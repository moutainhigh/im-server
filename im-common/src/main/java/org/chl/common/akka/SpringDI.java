package org.chl.common.akka;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

/**
 * @Auther: monster
 * @Date: 2019/12/17
 * @Description: 构造Props来创建Actor
 */
public class SpringDI implements IndirectActorProducer {
    private ApplicationContext context;
    private String beanName;

    public SpringDI(ApplicationContext context, String beanName) {
        this.context = context;
        this.beanName = beanName;
    }

    @Override
    public Actor produce() {
        return (Actor) context.getBean(beanName);
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) context.getType(beanName);
    }
}
