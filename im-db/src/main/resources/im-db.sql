CREATE DATABASE IF NOT EXISTS `im` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
use `im`;
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `nickname` varchar(16) COMMENT '昵称',
    `password` varchar(32) COMMENT '密码',
    `mailbox` varchar(32) COMMENT '邮箱',
    `create_time` datetime COMMENT '创建时间',
    `update_time` datetime COMMENT '更新时间',
    `login_time` datetime COMMENT '登录时间',
    PRIMARY KEY (`id`)
) COMMENT '用户表' ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;