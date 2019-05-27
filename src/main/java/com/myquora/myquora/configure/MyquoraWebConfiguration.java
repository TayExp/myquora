package com.myquora.myquora.configure;

import com.myquora.myquora.interceptor.LoginRequiredInterceptor;
import com.myquora.myquora.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;;

//直接实现WebMvcConfigurer接口的方式，而不用使用WebMvcConfigurerAdapter 
@Component
public class MyquoraWebConfiguration  implements WebMvcConfigurer{

	@Autowired
	PassportInterceptor passportInterceptor;
	
	@Autowired
	LoginRequiredInterceptor loginRequiredInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        // RequestInterceptor()为自己定义的拦截器
		//registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(passportInterceptor);
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
	}
	
//	@Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    }
	
}
