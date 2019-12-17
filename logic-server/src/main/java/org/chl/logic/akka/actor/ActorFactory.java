package org.chl.logic.akka.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.chl.common.constant.RemoteActorName;
import org.chl.logic.akka.config.RemoteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 远程接口与集群
 *
 * @author wang
 */
@Component
public class ActorFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ActorFactory.class);

    private final RemoteConfig remoteConfig;
    private final ActorSystem actorSystem;

    private String remoteUrl;
    private ActorRef clusterActor;
    private ActorRef monitorRemoteActor;
    private Map<String, ActorRef> remoteActorMap = new HashMap<>();

    @Autowired
    public ActorFactory(RemoteConfig remoteConfig, ActorSystem actorSystem) {
        this.remoteConfig = remoteConfig;
        this.actorSystem = actorSystem;
        this.remoteUrl = String.format(RemoteActorName.rootPath, RemoteActorName.sysActorName, remoteConfig.getIp(), remoteConfig.getPort());
        this.monitorRemoteActor = actorSystem.actorOf(Props.create(MonitorHttpActor.class, this), "monitorRemoteActor");
        this.clusterActor = actorSystem.actorOf(Props.create(ServerClusterActor.class, this), "clusterActor");
        LOG.info("初始化actor完成,远程调用根地址[{}]", remoteUrl);
    }

    public ActorSystem getSysActor() {
        return actorSystem;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public ActorRef getClusterActor() {
        return clusterActor;
    }

    public ActorRef getMonitorRemoteActor() {
        return monitorRemoteActor;
    }

    public ActorRef getRemoteHandler(String actorName) {
        String key = remoteUrl + actorName;
        if (remoteActorMap.containsKey(remoteUrl + actorName)) {
            return remoteActorMap.get(key);
        }
        return null;
    }

    public void setRemoteHandler(String actorName, ActorRef remeoteActor) {
        String key = remoteUrl + actorName;
        remoteActorMap.put(key, remeoteActor);
        LOG.warn("增加[{}] actor", key);
    }

    public void removeRemoteHandler(ActorRef actor) {
        remoteActorMap.remove(actor.path().toString());
        LOG.warn("删除[{}] actor", actor.path().toString());
    }

    public int getRemoteActorSize() {
        return remoteActorMap.size();
    }

    /**
     * 发送远程消息
     *
     * @param json
     * @param actorName
     */
    public void sendMsg(String json, String actorName) {
        ActorRef actorRef = this.getRemoteHandler(actorName);
        if (actorRef != null) {
            actorRef.tell(json, null);
        }
    }
}
