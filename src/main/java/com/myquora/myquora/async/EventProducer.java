package com.myquora.myquora.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.myquora.myquora.util.JedisAdapter;
import com.myquora.myquora.util.RedisKeyUtil;

@Service
public class EventProducer {

	@Autowired
	JedisAdapter jedisAdaptor;
	
	public boolean fireEvent(EventModel eventModel) {
		try {
			String json = JSONObject.toJSONString(eventModel);
			String key = RedisKeyUtil.getEventQueueKey(); // "EVENT_QUEUE"
			jedisAdaptor.lpush(key, json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
