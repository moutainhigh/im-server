package org.chl.common.email.service;

import com.alibaba.fastjson.JSONObject;
import org.chl.common.model.PairModel;

public interface EmailService {

	JSONObject sendVerifyCode(String mailbox);

	boolean validCode(String mailbox, String code);

	void sendEmail(PairModel<?, ?> model);
}
