package org.chl.db.data.mapper;

import org.apache.ibatis.annotations.Param;
import org.chl.db.data.dom.User;

public interface UserMapper {
    int insert(User user);

    int countByMailbox(@Param("mailbox") String mailbox);

    User selectByMailboxAndPassword(@Param("mailbox") String mailbox,@Param("password") String password);
}