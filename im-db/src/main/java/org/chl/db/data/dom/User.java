package org.chl.db.data.dom;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private String mailbox;

    private Date createtime;

    private Date updatetime;
}