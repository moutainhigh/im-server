package org.chl.login.dto;

import lombok.Data;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: 登入数据传输对象
 */
@Data
public class SigninDto {
    private String nickename;
    private String mailbox;
    private String token;

    public SigninDto(String nickename, String mailbox, String token) {
        this.nickename = nickename;
        this.mailbox = mailbox;
        this.token = token;
    }
}
