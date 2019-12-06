package org.chl.login.view;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: TODO
 */
public class LoginView {
    private String username;
    private String mailbox;
    private String token;

    public LoginView(String username, String mailbox, String token) {
        this.username = username;
        this.mailbox = mailbox;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
