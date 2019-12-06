package org.chl.login.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: 登入模型
 */
public class SigninModel {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱不合法")
    private String mailbox;

    @NotBlank(message = "密码不能为空")
    private String password;

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
