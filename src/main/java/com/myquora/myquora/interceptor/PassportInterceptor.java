package com.myquora.myquora.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.myquora.myquora.dao.LoginTicketDAO;
import com.myquora.myquora.dao.UserDAO;
import com.myquora.myquora.model.HostHolder;
import com.myquora.myquora.model.LoginTicket;
import com.myquora.myquora.model.User;

@Component
public class PassportInterceptor implements HandlerInterceptor{
	
	@Autowired
	private LoginTicketDAO loginTicketDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private HostHolder hostHolder;
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		hostHolder.clear();
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView != null && hostHolder.getUser() != null) {
			modelAndView.addObject("user", hostHolder.getUser());
		}
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ticket = null;
		if(request.getCookies() != null) {
			for(Cookie cookie : request.getCookies()) {
				if(cookie.getName().equals("ticket")) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		
		if(ticket != null) {
			LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
			if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
				return true;
			}
			// 把用户取出来，之后的页面都知道这个用户
			User user = userDAO.selectById(loginTicket.getUserId());
			// 利用了依赖注入的思想
			hostHolder.setUser(user);
		}
		return true;
		//返回false则整个请求就结束了，就不会再执行下去了
	}
	
	

}
