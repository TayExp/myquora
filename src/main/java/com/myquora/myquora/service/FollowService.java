package com.myquora.myquora.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.stereotype.Service;

import com.myquora.myquora.util.JedisAdapter;
import com.myquora.myquora.util.RedisKeyUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

@Service
public class FollowService {

	@Autowired
	JedisAdapter jedisAdaptor;
	
	public boolean follow(int userId, int entityType, int entityId) {
		// 某个实体的所有粉丝
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId); 
		// 某个用户对一类东西的关注
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType); 
		// 同一个时间
		Date date = new Date();
		Jedis jedis = jedisAdaptor.getJedis();
		Transaction tx = jedisAdaptor.multi(jedis);
		tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
		tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
		List<Object> ret = jedisAdaptor.exec(tx, jedis);
		return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(1) > 0;
	}
	
	public boolean unfollow(int userId, int entityType, int entityId) {
		// 某个实体的所有粉丝
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId); 
		// 某个用户对一类东西的关注
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType); 
		// 同一个时间
		Date date = new Date();
		// 同属一个事务
		Jedis jedis = jedisAdaptor.getJedis();
		Transaction tx = jedisAdaptor.multi(jedis);
		tx.zrem(followerKey, String.valueOf(userId));
		tx.zrem(followeeKey, String.valueOf(entityId));
		List<Object> ret = jedisAdaptor.exec(tx, jedis);
		return ret.size() == 2 && (Long)ret.get(0) > 0 && (Long)ret.get(1) > 0;
	}
	
	
	
	
	public List<Integer> getFollowers(int entityType, int entityId, int count){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return getIdsFromSet(jedisAdaptor.zrange(followerKey, 0, count));
	}
	
	public List<Integer> getFollowers(int entityType, int entityId, int offset, int count){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return getIdsFromSet(jedisAdaptor.zrange(followerKey, offset, offset + count));
	}
	
	public List<Integer> getFollowees(int userId, int entityType,  int count){
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return getIdsFromSet(jedisAdaptor.zrange(followeeKey, 0, count));
	}
	
	public List<Integer> getFollowees(int userId, int entityType, int offset, int count){
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return getIdsFromSet(jedisAdaptor.zrange(followeeKey, offset, offset + count));
	}
	
	public long getFollowerCount(int entityType, int entityId){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisAdaptor.zcard(followerKey);
	}
	
	public long getFolloweeCount(int userId, int entityType){
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return jedisAdaptor.zcard(followeeKey);
	}
	
	// 从集合中获取Id
	private List<Integer> getIdsFromSet(Set<String> idset) {
		List<Integer> ids = new ArrayList<>();
		for(String s : idset) {
			ids.add(Integer.parseInt(s));
		}
		return ids;
	}
	
	/**
	 * 判断一个用户是不是某个实体的粉丝
	 */
	public boolean isFollower(int userId, int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return jedisAdaptor.zscore(followerKey, String.valueOf(userId)) != null;
	}
}
