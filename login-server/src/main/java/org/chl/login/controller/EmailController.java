package org.chl.login.controller;

import com.alibaba.fastjson.JSONObject;
import org.chl.common.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/email")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendVerifyCode")
	JSONObject sendVerifyCode(String mailbox) {
		return emailService.sendVerifyCode(mailbox);
	}
}
