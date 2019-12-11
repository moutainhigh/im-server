package org.chl.login.akka.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.chl.common.constant.RemoteActorName;
import org.chl.login.akka.config.RemoteConfig;
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
    private Map<String, ActorRef> actorMap = new HashMap<>();
    private Map<String, ActorRef> remoteActorMap = new HashMap<>();
    /**
     * 远程监控actor
     */
    private ActorRef monitorRemoteActor;
    /**
     * 本地配置文件
     */
	private static final String SERVER_CFG_FILE = "login-server/src/main/resources/akka_http_server_actor.conf";

    @Autowired
    private RemoteConfig remoteConfig;

    @Autowired
    private void init() {
        remoteUrl = String.format(RemoteActorName.rootPath, RemoteActorName.sysActorName, remoteConfig.getIp(), remoteConfig.getPort());
        Config config = ConfigFactory.parseFile(new File(SERVER_CFG_FILE));
        sysActor = ActorSystem.create(RemoteActorName.sysActorName, config);
        monitorRemoteActor = sysActor.actorOf(Props.create(MonitorTcpActor.class, this), "monitorRemoteActor");
        clusterActor = sysActor.actorOf(Props.create(ServerClusterActor.class), "clusterActor");
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
        LOG.info("增加[{}] actor", key);
    }

    public void removeRemoteHandler(ActorRef actor) {
        remoteActorMap.remove(actor.path().toString());
        LOG.info("删除[{}] actor", actor.path().toString());
    }

    public void remoteClear() {
        remoteActorMap.clear();
        ;

    }

    public int getRemoteActorSize() {
        return remoteActorMap.size();
    }

    /**
     * 获取本地actor
     *
     * @param actorName
     * @return
     */
    public ActorRef getHandler(String actorName) {
        if (actorMap.containsKey(actorName)) {
            return actorMap.get(actorName);
        }
        return null;
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
