package org.chl.login.akka.actor;

import akka.actor.UntypedAbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import lombok.extern.slf4j.Slf4j;
/**
 * 集群，待扩展
 * @author wang
 *
 */
@Slf4j
public class ServerClusterActor extends UntypedAbstractActor {
	private Cluster cluster = Cluster.get(getContext().system());
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
			log.info("集群节点[{}]角色[{}]已经加入集群，等待确认服务确认",mp.member().address(),mp.member().roles());
		}else if(msg instanceof UnreachableMember) {
			UnreachableMember mp=(UnreachableMember) msg;
			log.info("集群节点[{}]角色[{}]已经被移除当前集群",mp.member().address(),mp.member().roles());
		}

	}

}
