package com.myquora.myquora.async.handler;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.myquora.myquora.async.EventHandler;
import com.myquora.myquora.async.EventModel;
import com.myquora.myquora.async.EventType;


@Component
public class LoginExceptionHandler implements EventHandler{
//	@Autowired
//	MailSender mailSender;

	@Override
	public void doHandle(EventModel model) {
		// 判读用户登录异常
//		Map<String, Object> map = new HashMap<>();
//		map.put("username", model.getExt("username"));
//		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//		simpleMailMessage.setTo("zhangdong@mochasoft.com.cn"); //接收人 
//		simpleMailMessage.setFrom("neuropathies@126.com"); // 发送人的别名 
//		simpleMailMessage.setSubject("测试邮件"); // 邮件主题 
//		simpleMailMessage.setText("这是一封测试邮件"); // 设置邮件内容 
//		mailSender.send(simpleMailMessage); //发送
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LOGIN);
	}
	
}
