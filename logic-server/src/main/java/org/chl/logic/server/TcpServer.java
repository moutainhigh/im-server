package org.chl.logic.server;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.chl.common.Game;
import org.chl.logic.event.manager.EventMgr;
import org.chl.logic.event.struct.net.ChannelCloseEvent;
import org.chl.logic.event.struct.net.ChannelConnectEvent;
import org.chl.logic.user.bo.UserBo;
import org.chl.message.CommMessage;
import org.chl.message.protocol.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: chenlin
 * @Date: 2020/1/2
 * @Description: TCP服务器
 */
@Slf4j
@Component
public class TcpServer {
    @Autowired
    private EventMgr eventMgr;
    @Autowired
    private ExecutorMgr executorMgr;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    public boolean isStop = false;

    public void start(int port) {
        MsgHandlerFactory.loadConfig();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128).handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("httpServerCodec", new HttpServerCodec());
                        ch.pipeline().addLast("httpObjectAggregator", new HttpObjectAggregator(1048576));
                        ch.pipeline().addLast("chunkedWriteHandler", new ChunkedWriteHandler());
                        ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new WebSocketFrameHandler());
                    }
                });
        Channel channel = null;
        try {
            channel = serverBootstrap.bind(port).sync().channel();
            log.info("游戏服务器启动成功，监听端口[{}]", port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("游戏服务器启动异常", e);
            System.exit(1);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop() {
        log.info("游戏服务器关闭中...");
        long now = System.currentTimeMillis();
        isStop = true;
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        executorMgr.shutdown();
        log.info("游戏服务器关闭成功,耗时[{}]毫秒", System.currentTimeMillis() - now);
    }

    private class WebSocketFrameHandler extends ChannelInboundHandlerAdapter {

        private WebSocketServerHandshaker handshaker;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            String ip = ((InetSocketAddress) channel.remoteAddress()).getHostString();
            UserBo userBo = new UserBo(channel, ip);
            channel.attr(UserBo.PLAYER_KEY).set(userBo);
            log.info("建立连接成功:[{}]", channel);
            eventMgr.post(new ChannelConnectEvent(userBo));
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.info("断开连接成功:[{}]", ctx.channel());
            UserBo userBo = ctx.channel().attr(UserBo.PLAYER_KEY).get();
            if (userBo != null && userBo.getUser() != null) {
                eventMgr.post(new ChannelCloseEvent(userBo));
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (isStop) {
                return;
            }
            if (msg instanceof FullHttpRequest) {
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            } else if (msg instanceof WebSocketFrame) {
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
            ReferenceCountUtil.release(msg);
        }

        private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
            if (!request.decoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))) {
                log.warn("HTTP解码失败");
                return;
            }
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    "ws://" + request.headers().get(HttpHeaderNames.HOST), null, false);
            handshaker = wsFactory.newHandshaker(request);
            if (handshaker == null) {
                log.warn("无法处理的websocket版本");
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), request);
                log.info("向客户端发送websocket握手,完成握手");
            }
        }

        private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
            Channel channel = ctx.channel();
            if (frame instanceof BinaryWebSocketFrame) {
                BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
                ByteBuf buf = binaryWebSocketFrame.content();
                byte[] req = new byte[buf.readableBytes()];
                buf.readBytes(req);
                try {
                    CommMessage.Comm message = CommMessage.Comm.parseFrom(req);
                    dispatch(channel.attr(UserBo.PLAYER_KEY).get(), message);
                } catch (InvalidProtocolBufferException e) {
                    log.warn("会话[{}]消息[{}]格式不正确", channel, JSON.toJSONString(binaryWebSocketFrame));
                    ctx.close();
                }
            }
        }

        private void dispatch(UserBo userBo, CommMessage.Comm message) {
            int moduleId = message.getMsgId() / 100;
            if (message.getMsgId() == Protocol.ReqPing) {
                userBo.channel.write(getPONGMSG());
            } else if (moduleId == MsgHandlerFactory.getModuleId("login")) {
                executorMgr.getLoginExecutor(userBo.channel).execute(new ReqMsgTask(userBo, message));
            } else if (moduleId == MsgHandlerFactory.getModuleId("bank")
                    || moduleId == MsgHandlerFactory.getModuleId("customer")
                    || moduleId == MsgHandlerFactory.getModuleId("email")
                    || moduleId == MsgHandlerFactory.getModuleId("player")
                    || moduleId == MsgHandlerFactory.getModuleId("rank")
                    || moduleId == MsgHandlerFactory.getModuleId("redpackage")) {// 广场消息(邮件、消息、公告、银行、背包、商城等)
                executorMgr.getPlazaExecutor(userBo.getId()).execute(new ReqMsgTask(userBo, message));
            } else if (moduleId == MsgHandlerFactory.getModuleId(Game.TOTE.moduleId)) {// 游戏消息(扎金花、斗地主、捕鱼等),根据桌子分发
                // 根据游戏分发
                executorMgr.getGameExecutor(moduleId).execute(new ReqMsgTask(userBo, message));
            } else {
                log.warn("消息id[{}]无法找到对应的executor,请按规则定义", message.getMsgId());
                userBo.channel.close();
            }
        }

        public CommMessage.Comm getPONGMSG() {
            CommMessage.Comm.Builder commonMessage = CommMessage.Comm.newBuilder();
            commonMessage.setMsgId(Protocol.ResPong);
            return commonMessage.build();
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            super.userEventTriggered(ctx, evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }
}
