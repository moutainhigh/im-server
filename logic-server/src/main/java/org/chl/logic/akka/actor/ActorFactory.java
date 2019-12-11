package org.chl.logic.akka.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.chl.common.constant.RemoteActorName;
import org.chl.logic.akka.config.RemoteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
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
    /**
     * 远程调用根地址
     */
    private String remoteUrl;
    /**
     * 根actor
     */
    private ActorSystem sysActor;
    /**
     * 集群扩展atcor
     */
    private ActorRef clusterActor;
    /**
     * 本地actor
     */
    private Map<String, ActorRef> actorMap = new HashMap<>();
    /**
     * 远程http actor
     */
    private Map<String, ActorRef> remoteActorMap = new HashMap<>();
    /**
     * 远程监控actor
     */
    private ActorRef monitorRemoteActor;

    /**
     * 本地配置文件
     */
    private static final String SERVER_CFG_FILE = "logic-server/src/main/resources/akka_tcp_server_actor.conf";

    @Autowired
    private RemoteConfig remoteConfig;

    @Autowired
    public void init() {
        remoteUrl = String.format(RemoteActorName.rootPath, RemoteActorName.sysActorName, remoteConfig.getIp(), remoteConfig.getPort());
        Config config = ConfigFactory.parseFile(new File(SERVER_CFG_FILE));
        sysActor = ActorSystem.create(RemoteActorName.sysActorName, config);
        monitorRemoteActor = sysActor.actorOf(Props.create(MonitorHttpActor.class, this), "monitorRemoteActor");
        clusterActor = sysActor.actorOf(Props.create(ServerClusterActor.class, this), "clusterActor");
        //登录
//        actorMap.put(RemoteActorName.tcploginActor, sysActor.actorOf(Props.create(LoginActor.class), RemoteActorName.tcploginActor));
        LOG.info("初始化actor完成,远程调用根地址[{}],系统配置文件[{}]", remoteUrl, config.toString());
    }

    public ActorSystem getSysActor() {
        return sysActor;
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
