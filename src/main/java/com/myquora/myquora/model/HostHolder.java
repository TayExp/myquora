package com.myquora.myquora.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {

	// 方便多线程访问——线程本地/变量副本
	// 每个线程都拷贝这样一个变量
	private static ThreadLocal<User> users = new ThreadLocal<>();

	// 根据当前线程找到user
	public User getUser() {
		return users.get();
	}

	public void setUser(User user) {
		users.set(user);
	}
	
	public void clear() {
		users.remove();
	}
}
