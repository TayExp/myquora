package com.myquora.myquora.async.handler;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.myquora.myquora.async.EventHandler;
import com.myquora.myquora.async.EventModel;
import com.myquora.myquora.async.EventType;
import com.myquora.myquora.model.EntityType;
import com.myquora.myquora.model.Feed;
import com.myquora.myquora.model.Question;
import com.myquora.myquora.model.User;
import com.myquora.myquora.service.*;
import com.myquora.myquora.util.JedisAdapter;
import com.myquora.myquora.util.RedisKeyUtil;

/**
 * 
 * @author miaohj
 * 把新鲜事加进feed里
 */
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;
    
    @Autowired
    FollowService followService;
    
    @Autowired
    QuestionService questionService;
	
	@Override
	public void doHandle(EventModel model) {
		// 测试用途：随机得到userId
//		java.util.Random rand = new java.util.Random();
//		model.setActorId(1+rand.nextInt(18));
		// 构造一个feed
		Feed feed = new Feed();
		feed.setCreatedDate(new Date());
		// follow 4 comment 1
		feed.setType(model.getType().getValue());
		System.out.println("新增的feed是类型是"+feed.getType());
		feed.setUserId(model.getActorId());
		feed.setData(buildFeedData(model));
		if(feed.getData() == null) {
			return;
		}
		feedService.addFeed(feed);
		// 获取所有粉丝
		List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,
				model.getActorId(), Integer.MAX_VALUE);
		// 系统消息
		followers.add(0);
		// 给followers推
		for(int follower : followers) {
			String timelineKey = RedisKeyUtil.getTimelineKey(follower);
			jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
			// 可以在这里限制最长长度
		}
	}

	private String buildFeedData(EventModel model) {
		// 建立feed的内容
		Map<String, String> map = new HashMap<>();
		// 触发用户
		User actor = userService.getUser(model.getActorId());
		if(actor != null) {
			map.put("userId", String.valueOf(actor.getId()));
			map.put("userHead", actor.getHeadUrl());
	        map.put("userName", actor.getName());
	        if(model.getType() == EventType.COMMENT || // 目前评论的实体只有question
	        	(model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)){
	        	Question question = questionService.getById(model.getEntityId());
	        	if (question == null) {
	                return null;
	            }
	            map.put("questionId", String.valueOf(question.getId()));
	            map.put("questionTitle", question.getTitle());
	            return JSONObject.toJSONString(map);
	        }
		}
		
		return null;
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(new EventType[] {EventType.FOLLOW, EventType.COMMENT});
	}

}
