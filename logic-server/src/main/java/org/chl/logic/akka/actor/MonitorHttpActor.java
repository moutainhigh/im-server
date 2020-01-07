package org.chl.logic.akka.actor;

import akka.actor.*;
import org.chl.common.constant.RemoteActorName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 * 远程接口监控
 * @author wang
 *
 */
public class MonitorHttpActor extends UntypedAbstractActor {
	private static final Logger LOG = LoggerFactory.getLogger(MonitorHttpActor.class);
	private final ActorFactory actorFactory;

	public MonitorHttpActor(ActorFactory actorFactory) {
		super();
		this.actorFactory = actorFactory;
	}
	
	/**
	 * 接受三中消息
	 *           1:路径参数，搜索远程路径actor是否可用
	 *           2:ActorIdentity,远程确认
	 *           3:Terminated,远程关闭
	 */
	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof String) {
			Field[] fields = RemoteActorName.class.getDeclaredFields();
			try {
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.getType().toString().endsWith("java.lang.String")
							&& Modifier.isStatic(field.getModifiers())) {
						String fieldName = field.get(RemoteActorName.class).toString();
						if(!fieldName.endsWith(RemoteActorName.rootPath)&&!fieldName.equals(RemoteActorName.httpActorName)&&fieldName.contains(RemoteActorName.httpActorName)) {
							ActorSelection selection = getContext()
									.actorSelection(actorFactory.getRemoteUrl() + fieldName);
							selection.tell(new Identify(fieldName), getSelf());
						}
						
					}
				}
			} catch (Exception e) {
				LOG.error("初始化远程调用借口失败[{}]", e.getMessage());
			}
		} else if (msg instanceof ActorIdentity) {
			ActorIdentity aiy = (ActorIdentity) msg;
			String remoteActorName=aiy.correlationId().toString();
			
				if (aiy.getRef() == null) {
					LOG.warn("远程服务[{}]未开启", remoteActorName);
				} else {
					ActorRef remeoteActor = aiy.getRef();
					getContext().watch(remeoteActor);
					actorFactory.setRemoteHandler(remoteActorName, remeoteActor);
					LOG.info("远程服务[{}]已经开启", remeoteActor.path());
				}
			
		} else if (msg instanceof Terminated) {
			Terminated terminated = (Terminated) msg;
			actorFactory.removeRemoteHandler(terminated.getActor());
			LOG.info("远程服务[{}]已经移除，剩余服务[{}]个", terminated.getActor().path().toString(),
					actorFactory.getRemoteActorSize());
		}else if(msg instanceof Exception) {
			LOG.info("远程服务[{}]异常", getSender());
		}
		
	}

}
