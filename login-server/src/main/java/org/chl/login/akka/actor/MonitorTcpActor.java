package org.chl.login.akka.actor;

import akka.actor.*;
import akka.remote.AssociatedEvent;
import akka.remote.AssociationErrorEvent;
import akka.remote.DisassociatedEvent;
import akka.remote.RemotingLifecycleEvent;
import lombok.extern.slf4j.Slf4j;
import org.chl.common.constant.RemoteActorName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 * 远程接口监控
 * @author wang
 *
 */
@Slf4j
public class MonitorTcpActor extends UntypedAbstractActor {
	private final ActorFactory actorFactory;

	public MonitorTcpActor(ActorFactory actorFactory) {
		super();
		this.actorFactory = actorFactory;
	}
	@Override
	public void preStart() throws Exception {
		// 订阅远程事件
		 getContext().system().eventStream().subscribe(getSelf(), RemotingLifecycleEvent.class);

	}
	/**
	 * 接受三中消息
	 *           1:路径参数，搜索远程路径actor是否可用
	 *           2:ActorIdentity,远程确认
	 *           3:Terminated,远程关闭
	 */
	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof AssociatedEvent) {// 连接
			AssociatedEvent aevent=(AssociatedEvent) msg;
			Field[] fields = RemoteActorName.class.getDeclaredFields();
			try {
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.getType().toString().endsWith("java.lang.String")
							&& Modifier.isStatic(field.getModifiers())) {
						String fieldName = field.get(RemoteActorName.class).toString();
						if(!fieldName.endsWith(RemoteActorName.rootPath)&&!fieldName.equals(RemoteActorName.tcpActorName)&&fieldName.contains(RemoteActorName.tcpActorName)) {
							ActorSelection selection = getContext()
									.actorSelection(actorFactory.getRemoteUrl() + fieldName);
							selection.tell(new Identify(fieldName), getSelf());
						}
					}
				}
			} catch (Exception e) {
				log.error("初始化远程调用借口失败[{}]", e.getMessage());
			}
			log.info("远程remote[{}][{}]已经开启",aevent.getRemoteAddress(),aevent.getLocalAddress());
		} else if (msg instanceof ActorIdentity) {
			ActorIdentity aiy = (ActorIdentity) msg;
			String remoteActorName=aiy.correlationId().toString();
				if (aiy.getRef() == null) {
					log.warn("远程服务[{}]未开启", remoteActorName);
				} else {
					ActorRef remeoteActor = aiy.getRef();
					actorFactory.setRemoteHandler(remoteActorName, remeoteActor);
					log.info("远程服务[{}]已经开启", remoteActorName);
				}
		} else if (msg instanceof DisassociatedEvent) {// 断开连接
			DisassociatedEvent event = (DisassociatedEvent) msg;
			actorFactory.remoteClear();
			log.info("远程remote服务[{}]已经移除，剩余服务[{}]个",event.getRemoteAddress(),actorFactory.getRemoteActorSize());
		}else if(msg instanceof Exception) {
			log.info("远程服务[{}]异常", getSender());
		}else  if(msg instanceof AssociationErrorEvent){// 连接错误
			AssociationErrorEvent event = (AssociationErrorEvent) msg;
			log.error("远程服务[{}][{}][{}]异常", event.getRemoteAddress(),event.getLocalAddress(),event.getCause().getMessage());
		}else{
			log.info("远程服务[{}]未定义", msg);
		}
	}
}
