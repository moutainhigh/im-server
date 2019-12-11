package org.chl.logic.akka.actor;

import akka.actor.UntypedAbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import org.chl.common.constant.RemoteActorName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 集群监控
 * @author wang
 *
 */
public class ServerClusterActor extends UntypedAbstractActor {
	private static final Logger LOG = LoggerFactory.getLogger(ServerClusterActor.class);
	private Cluster cluster = Cluster.get(getContext().system());
	private final ActorFactory actorFactory;
	public ServerClusterActor(ActorFactory actorFactory) {
		super();
		this.actorFactory = actorFactory;
	}
	/**
	 * 取消订阅
	 */
	@Override
	public void postStop() throws Exception {
		cluster.unsubscribe(getSelf());
	}
	/**
	 * 订阅集群成员事件
	 */
	@Override
	public void preStart() throws Exception {
		cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), UnreachableMember.class, MemberEvent.class);
	}
	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg instanceof MemberUp) {
			MemberUp mp=(MemberUp) msg;
			//http加入集群，检查远程remote接口是否开启
			for(String role:mp.member().getRoles()) {
				if(role.contains(RemoteActorName.httpActorName)) {
					actorFactory.getMonitorRemoteActor().tell("start monitor RemoteActor", null);
					break;
				}
			}
			LOG.info("服务节点[{}]角色[{}]已经加入集群，等待确认服务确认",mp.member().address(),mp.member().roles());
		}else if(msg instanceof UnreachableMember) {
			UnreachableMember mp=(UnreachableMember) msg;
			LOG.info("服务节点[{}]角色[{}]已经被移除当前集群",mp.member().address(),mp.member().roles());
		}

	}

}
