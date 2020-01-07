package org.chl.db.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "tb_user")
public class User{

    @TableId(value = "id")
    private Long id;

    @TableField(value = "nickname")
    private String nickname;

    @TableField(value = "password")
    private String password;

    @TableField(value = "mailbox")
    private String mailbox;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "login_time")
    private Date loginTime;

    @TableField(exist = false)
    private String loginKey;
}