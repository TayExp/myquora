package com.myquora.myquora.util;

import com.myquora.myquora.model.User;

public class RedisKeyUtil {

	// 生成key的名字，保证不重复，前缀都是业务
	private static String SPLIT = ":";
	private static String BIZ_LIKE = "LIKE";
	private static String BIZ_DISLIKE = "DISLIKE";
	private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
	// 关注他/它的对象
	private static String BIZ_FOLLOWER = "FOLLOWER";
	// 被他关注的对象
	private static String BIZ_FOLLOWEE = "FOLLOWEE";
	private static String BIZ_TIMELINE = "TIMELINE";
	
	public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}
	
	public static String getDisLikeKey(int entityType, int entityId) {
		return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}
	
	public static String getEventQueueKey() {
		return BIZ_EVENTQUEUE;
	}
	
	// 这个实体对象的所有粉丝
	public static String getFollowerKey(int entityType, int entityId) {
		return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId); 
	}
	
	//userId这个人对某类实体的关注情况的key
	public static String getFolloweeKey(int userId, int entityType) {
		return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
	}
	
	public static String getTimelineKey(int userId) {
		return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
	}
}
