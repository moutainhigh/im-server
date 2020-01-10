package org.chl.common.email.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chl.common.config.RedisConfig;
import org.chl.common.constant.CommonConstant;
import org.chl.common.email.config.EmailConfig;
import org.chl.common.email.service.EmailService;
import org.chl.common.model.PairModel;
import org.chl.common.mq.rabbit.producer.EmailProducer;
import org.chl.common.util.RandomKeyGenerator;
import org.chl.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailConfig emailConfig;
	@Resource
	private RedisTemplate<String, PairModel<?, ?>> redisTemplate;
	@Autowired
	private EmailProducer emailProducer;

	@Override
	public JSONObject sendVerifyCode(String mailbox) {
		if (StringUtils.isBlank(mailbox)) {
			return ResultUtil.failure("邮箱不能为空");
		}
		if (!mailbox.matches(CommonConstant.MAILBOX_PATTERN)) {
			return ResultUtil.failure("邮箱不合法");
		}
		emailProducer.sendMsg(new PairModel<>(mailbox, RandomKeyGenerator.generateNum(6)));
		return ResultUtil.success(null, "操作成功");
	}

	@Override
	public boolean validCode(String mailbox, String code) {
		if (!redisTemplate.opsForHash().get(RedisConfig.REDIS_KEY_EMAIL, mailbox).equals(code)) {
			return false;
		}
		return true;
	}

	@Override
	public void sendEmail(PairModel<?, ?> model) {
		Properties properties = new Properties();
		properties.put("mail.transport.protocol", emailConfig.getProtocol());
		properties.put("mail.smtp.host", emailConfig.getHost());
		properties.put("mail.smtp.port", emailConfig.getPort());
		properties.put("mail.smtp.auth", emailConfig.getAuth());
		properties.put("mail.smtp.ssl.enable", emailConfig.getSsl());
		properties.put("mail.debug", emailConfig.getDebug());
		Session session = Session.getInstance(properties);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(emailConfig.getFrom()));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress((String) model.v1));
			message.setSubject("邮件标题");
			message.setText((String) model.v2);
			Transport transport = session.getTransport();
			transport.connect(emailConfig.getFrom(), emailConfig.getAuthCode());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (MessagingException e) {
			log.error("MessagingException:", e);
			return;
		}
		redisTemplate.opsForHash().put(RedisConfig.REDIS_KEY_EMAIL, model.v1, model.v2);
	}
}
