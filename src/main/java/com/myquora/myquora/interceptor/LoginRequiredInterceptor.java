package com.myquora.myquora.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.myquora.myquora.model.HostHolder;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

	@Autowired
	HostHolder hostHolder;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(hostHolder.getUser() == null) {
			response.sendRedirect("/reglogin?next=" + request.getRequestURI());
			//return false;
		}
		return true;
	}

}
