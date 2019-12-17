package org.chl.common.akka;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtendedActorSystem;

/**
 * @Auther: monster
 * @Date: 2019/12/17
 * @Description: 通过继承AbstractExtensionId，我们可以在ActorSystem范围内创建并查找SpringExt
 */
public class SpringExtProvider extends AbstractExtensionId<SpringExt> {

    private static SpringExtProvider provider = new SpringExtProvider();

    @Override
    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }

    public static SpringExtProvider getInstance(){
        return provider;
    }
}
