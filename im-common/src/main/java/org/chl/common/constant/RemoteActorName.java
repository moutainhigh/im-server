package org.chl.common.constant;

/**
 * @Auther: monster
 * @Date: 2019/12/9
 * @Description: TODO
 */
public class RemoteActorName {
    /**
     * actor路径
     */
    public static final String rootPath="akka.tcp://%s@%s:%s/user/";
    /**
     * 根名字
     */
    public static final String sysActorName="sys";
    public static final String tcpActorName="tcp";
    public static final String httpActorName="http";
    /**
     *
     */
    public static final String tcploginActor="tcploginActor";
    /**
     * 增加用户群
     */
    public static final String httpUserAddGroupActor="httpUserAddGroupActor";
    /**
     * 删除用户群
     */
    public static final String httpUserDelGroupActor="httpUserDelGroupActor";
}
