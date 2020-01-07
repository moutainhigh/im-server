package org.chl.login.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Auther: monster
 * @Date: 2019/12/6
 * @Description: 注册值对象
 */
@Data
public class RegisterVo {

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱不合法")
    private String mailbox;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
