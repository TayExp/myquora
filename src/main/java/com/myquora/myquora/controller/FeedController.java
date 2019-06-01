package com.myquora.myquora.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.aspectj.weaver.ArrayAnnotationValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myquora.myquora.model.EntityType;
import com.myquora.myquora.model.Feed;
import com.myquora.myquora.model.HostHolder;
import com.myquora.myquora.service.FeedService;
import com.myquora.myquora.service.FollowService;
import com.myquora.myquora.util.JedisAdapter;
import com.myquora.myquora.util.RedisKeyUtil;

/**
 * 
 * @author miaohj
 *
 */
@Controller
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;
    
    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPushFeeds(Model model) {
    	// 为什么将未登录设置为0id
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<Feed>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
    
    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPullFeeds(Model model) {
    	int hostUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId():0; 
    	
    	List<Integer> followees = new ArrayList<>();
    	if(hostUserId != 0) {
        	followees = followService.getFollowees(hostUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
    	}
    	List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(hostUserId),	0, 10);
		List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
		model.addAttribute("feeds", feeds );
    	return "feeds";
    }
}
