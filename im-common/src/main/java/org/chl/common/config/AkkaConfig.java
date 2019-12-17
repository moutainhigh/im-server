package org.chl.common.config;

import akka.actor.ActorSystem;
import org.chl.common.akka.SpringExtProvider;
import org.chl.common.constant.RemoteActorName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: monster
 * @Date: 2019/12/17
 * @Description: TODO
 */
@Configuration
public class AkkaConfig {

    private final ApplicationContext context;

    @Autowired
    public AkkaConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public ActorSystem createActorSystem(){
        ActorSystem actorSystem = ActorSystem.create(RemoteActorName.sysActorName);
        SpringExtProvider.getInstance().get(actorSystem).init(context);
        return actorSystem;
    }
}
