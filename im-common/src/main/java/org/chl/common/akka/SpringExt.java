package org.chl.common.akka;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;

/**
 * @Auther: monster
 * @Date: 2019/12/17
 * @Description: 扩展组件，构造Props,用于生产ActorRef
 */
public class SpringExt implements Extension {
    private ApplicationContext context;

    public void init(ApplicationContext context){
        System.out.println("applicationContext初始化...");
        this.context = context;
    }

    /**
     * @Author monster
     * @Description 该方法用来创建Props对象，依赖前面创建的DI组件，获取到Props对象，我们就可以创建Actor bean对象
     * @Date 2019/12/17
     * @Param [beanName]
     * @return akka.actor.Props
     **/
    public Props create(String beanName){
        return Props.create(SpringDI.class,this.context,beanName);
    }
}
