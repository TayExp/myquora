package com.myquora.myquora.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import com.myquora.myquora.dao.LoginTicketDAO;
import com.myquora.myquora.dao.UserDAO;
import com.myquora.myquora.model.LoginTicket;
import com.myquora.myquora.model.User;
import com.myquora.myquora.util.MyUtil;

@Service
public class UserService {
	 @Autowired
	 private UserDAO userDAO;

	 // service层可以调用所有dao
	@Autowired
	private LoginTicketDAO loginTicketDAO;
	 
     public User getUser(int id) {
        return userDAO.selectById(id);
     }

	public Map<String, Object> register(String username, String password) {
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空");
			return  map;
		}
		if(StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return  map;
		}
		User user = userDAO.selectByName(username);
		if(user != null) {
			map.put("msg", "用户民已经被注册");
			return map;
		}
		
		//密码强度
		user = new User();
		user.setName(username);
		//随机生成长度为5的字符串为salt
		// UUID:universally unique identifier (UUID).A UUID represents a 128-bit value. 
		user.setSalt(UUID.randomUUID().toString().substring(0, 5));
		user.setPassword(MyUtil.MD5(password+user.getSalt()));
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		userDAO.addUser(user);
		//登录
		String ticket = addLoginTicket(user.getId());
		map.put("ticket", ticket); //response回去，注册完就登录
		return map;
	}



	public Map<String, Object> login(String username, String password) {
		Map<String, Object> map = new HashMap<>();
		if(StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空");
			return  map;
		}
		if(StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return  map;
		}
		User user = userDAO.selectByName(username);
		if(user == null) {
			map.put("msg", "用户民不存在");
			return map;
		}
		
		if(!MyUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
			map.put("msg", "密码错误");
			return map;
		}
		//登录成功，加个ticket票
		String ticket = addLoginTicket(user.getId());
		map.put("ticket", ticket); 
		return map;
	} 
	
	// 下发ticket
	public String addLoginTicket(int userId) {
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(userId);
		Date now = new Date();
		now.setTime(3600*24*100+now.getTime());
		loginTicket.setExpired(now);
		loginTicket.setStatus(0);
		loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		loginTicketDAO.addTicket(loginTicket);
		return loginTicket.getTicket();
	}

	public void logout(String ticket) {
		loginTicketDAO.updateStatus(ticket,1);
	}
	
}
