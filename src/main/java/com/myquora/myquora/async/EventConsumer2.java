package com.myquora.myquora.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.myquora.myquora.util.JedisAdapter;
import com.myquora.myquora.util.RedisKeyUtil;

@Service
public class EventConsumer2 implements InitializingBean, ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(EventConsumer2.class);
	private Map<EventType, List<EventHandler>> config = new HashMap<>();
	private ApplicationContext applicationContext;
	
	@Autowired
	JedisAdapter jedisAdaptor;
	
	@Override
	public void afterPropertiesSet() throws Exception{
		// 获取所有的Handler
		Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
		if(beans != null) {
			// 从SupportEventType 到  config
			for(Map.Entry<String, EventHandler> entry : beans.entrySet()) {
				List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
				for(EventType type : eventTypes) {
//					config.getOrDefault(type, new ArrayList<>()).add(entry.getValue()); //为什么不行？
					if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
				}
			}
		}
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					String key = RedisKeyUtil.getEventQueueKey();
					List<String> events = jedisAdaptor.brpop(0, key);
					for(String message : events) {
						// 第一个是key 跳过
						if(message.equals(key)) {
							continue;
						}
						// 反序列化，生成EventModel类型
						EventModel eventModel = JSON.parseObject(message, EventModel.class);
						if(!config.containsKey(eventModel.getType())) {
							logger.error("不能识别的事件");
							continue;
						} 
						for(EventHandler handler : config.get(eventModel.getType())) {
							handler.doHandle(eventModel);
						}
					}
				}
			}
		});
		thread.start();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
