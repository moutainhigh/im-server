package org.chl.db.data.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String nickename;
    private String password;
    private String mailbox;
    private Date createtime;
    private Date updatetime;
}