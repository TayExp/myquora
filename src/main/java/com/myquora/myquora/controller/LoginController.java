package com.myquora.myquora.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.myquora.myquora.service.UserService;


@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	UserService userService;
	
	@RequestMapping(path = {"/login/"}, method = {RequestMethod.POST}) //带着提交的用户名密码等信息
	public String login(Model model, 
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value="next", required=false) String next,
			@RequestParam(value="rememberme", defaultValue = "false")boolean rememberme, // 有默认值显式写出来value参数
			HttpServletResponse response //下发ticket到cookie里
			) {
			// 为userservice添加register方法
		try {	
			Map<String, Object> map = userService.login(username, password); //加一个login的服务
			if(map.containsKey("ticket")) { 
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if (rememberme) {
					cookie.setMaxAge(3600*24*5);
	            }
				response.addCookie(cookie);
				if(StringUtils.isNotBlank(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			}else {  //不包含ticket登录失败
				model.addAttribute("msg", map.get("msg"));
				return "login";
			}
			
		} catch (Exception e) {
			logger.error("登录异常" + e.getMessage());
			return "login";
		}
	}
	
	// form.action='/reg/'">注册
	@RequestMapping(path = {"/reg/"}, method = {RequestMethod.POST}) //带着提交的用户名密码等信息
	public String reg(Model model, 
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam(value="next",required=false) String next,
			@RequestParam(value="rememberme", defaultValue = "false")boolean rememberme, // 有默认值显式写出来value参数
			HttpServletResponse response //下发ticket到cookie里
			) {
			// 为userservice添加register方法
		try {	
			Map<String, Object> map = userService.register(username, password); //加一个login的服务
			if(map.containsKey("ticket")) { 
				Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if(rememberme) {
					cookie.setMaxAge(3600*24*5);
				}
				response.addCookie(cookie);
				if(StringUtils.isNotBlank(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			}else {  //不包含ticket登录失败
				model.addAttribute("msg", map.get("msg"));
				return "login";
			}
			
		} catch (Exception e) {
			logger.error("注册异常" + e.getMessage());
			model.addAttribute("msg", "服务器异常");
			return "login";
		}
	}
	
	// form.action='/login/'">登录
	// form.action='/reg/'">注册
	// 登录注册页面
	@RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
	public String reg(Model model, @RequestParam(value = "next", required = false)String next) {
		model.addAttribute("next", next);
		return "login";
	}
	
	//登出
	@RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String logout(@CookieValue("ticket") String ticket) {
		userService.logout(ticket);
		return "redirect:/";
	}
	
}
