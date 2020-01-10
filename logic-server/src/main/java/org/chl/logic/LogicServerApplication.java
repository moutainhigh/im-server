package org.chl.logic;

import lombok.extern.slf4j.Slf4j;
import org.chl.logic.config.NettyConfig;
import org.chl.logic.server.TcpServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan("org.chl.db.data.mapper")
@SpringBootApplication(scanBasePackages = {"org.chl"})
public class LogicServerApplication implements CommandLineRunner {

    private final TcpServer tcpServer;
    private final NettyConfig nettyConfig;

    @Autowired
    public LogicServerApplication(TcpServer tcpServer, NettyConfig nettyConfig) {
        this.tcpServer = tcpServer;
        this.nettyConfig = nettyConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(LogicServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                tcpServer.stop();
            }, "closeServer"));
            tcpServer.start(nettyConfig.getPort());
        } catch (Exception e) {
            log.error("游戏服务器启动异常", e);
            System.exit(1);
        }
    }
}
