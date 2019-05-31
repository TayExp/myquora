package com.myquora.myquora.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.myquora.myquora.async.EventModel;
import com.myquora.myquora.async.EventProducer;
import com.myquora.myquora.async.EventType;
import com.myquora.myquora.model.Comment;
import com.myquora.myquora.model.EntityType;
import com.myquora.myquora.model.HostHolder;
import com.myquora.myquora.model.Question;
import com.myquora.myquora.model.User;
import com.myquora.myquora.model.ViewObject;
import com.myquora.myquora.service.CommentService;
import com.myquora.myquora.service.FollowService;
import com.myquora.myquora.service.QuestionService;
import com.myquora.myquora.service.SensitiveService;
import com.myquora.myquora.service.UserService;
import com.myquora.myquora.util.MyUtil;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

@Controller

public class FollowController {

	private static final Logger logger = LoggerFactory.getLogger(FollowController.class);
	
    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

	
	@RequestMapping(value = "/followUser", method = {RequestMethod.POST})
	@ResponseBody
	public String followUser(@RequestParam("userId")int userId) {
		if(hostHolder.getUser() == null) {
			return MyUtil.getJSONString(999); //未登录
		}
		int hostUserId = hostHolder.getUser().getId();
		boolean ret = followService.follow(hostUserId, EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW) //follow事件
				 .setActorId(hostUserId).setEntityId(userId) //关注人和被关注人
	             .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
		// 返回hostUser关注的总人数
		return MyUtil.getJSONString(ret ? 0:1,
				String.valueOf(followService.getFolloweeCount(hostUserId, EntityType.ENTITY_USER))); 
	}
	
	@RequestMapping(value = "/unfollowUser", method = {RequestMethod.POST})
	@ResponseBody
	public String unfollowUser(@RequestParam("userId")int userId) {
		if(hostHolder.getUser() == null) {
			return MyUtil.getJSONString(999); //未登录
		}
		int hostUserId = hostHolder.getUser().getId();
		boolean ret = followService.unfollow(hostUserId, EntityType.ENTITY_USER, userId);
		eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW) //follow事件
				 .setActorId(hostUserId).setEntityId(userId) //关注人和被关注人
	             .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
		// 返回hostUser关注的总人数
		return MyUtil.getJSONString(ret ? 0:1,
				String.valueOf(followService.getFolloweeCount(hostUserId, EntityType.ENTITY_USER))); 
	}
	
	@RequestMapping(value = "/followQuestion", method = {RequestMethod.POST})
	@ResponseBody
	public String followQuestion(@RequestParam("questionId")int questionId) {
		User hostUser = hostHolder.getUser();
		if(hostUser == null) {
			return MyUtil.getJSONString(999); //未登录
		}
		Question q = questionService.getById(questionId);
		if(q == null) {
			return MyUtil.getJSONString(1, "问题不存在"); // 如果问题不存在
		}
		boolean ret = followService.follow(hostUser.getId(), EntityType.ENTITY_QUESTION, questionId);
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW) //follow事件
				 .setActorId(hostUser.getId()).setEntityId(questionId) //关注人和被关注问题
	             .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUserId()));
		Map<String, Object> info = new HashMap<>();
		// 关注这个问题的用户的头像，需要显示在该问题下面
		info.put("headUrl", hostUser.getHeadUrl());
		info.put("name", hostUser.getName());
		info.put("id", hostUser.getId());
		// 关注该问题的总人数
		info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
		return MyUtil.getJSONString(ret ? 0:1, info); 
	}
	
	@RequestMapping(value = "/unfollowQuestion", method = {RequestMethod.POST})
	@ResponseBody
	public String unfollowQuestion(@RequestParam("questionId")int questionId) {
		User hostUser = hostHolder.getUser();
		if(hostUser == null) {
			return MyUtil.getJSONString(999); //未登录
		}
		Question q = questionService.getById(questionId);
		if(q == null) {
			return MyUtil.getJSONString(1, "问题不存在"); // 如果问题不存在
		}
		boolean ret = followService.unfollow(hostUser.getId(), EntityType.ENTITY_QUESTION, questionId);
		eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW) //follow事件
				 .setActorId(hostUser.getId()).setEntityId(questionId) //关注人和被关注问题
	             .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(q.getUserId()));
		Map<String, Object> info = new HashMap<>();
		info.put("id", hostUser.getId());
		// 关注该问题的总人数
		info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
		return MyUtil.getJSONString(ret ? 0:1, info); 
	}
	
	// 订阅的信息
	@RequestMapping(path= {"/user/{uid}/followees"}, method = {RequestMethod.GET})
	public String followees(Model model, @PathVariable("uid")int userId) {
		List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0 ,10);
		if(hostHolder.getUser() != null) {
			// hostuserId参数主要用来判断是否已关注
			model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
		} else {
			model.addAttribute("followees", getUsersInfo(0, followeeIds));
		}
		model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
		// 注意区分hostuser 和 curuser  前者是本地用户，输了用户密码的，  后者是当前网页的用户 正在查看的主页
		model.addAttribute("curUser", userService.getUser(userId));
		return "followees";
	}
	
	// 粉丝的信息
	@RequestMapping(path= {"/user/{uid}/followers"}, method = {RequestMethod.GET})
	public String followers(Model model, @PathVariable("uid")int userId) {
		List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0 ,10);
		if(hostHolder.getUser() != null) {
			// hostuserId参数主要用来判断是否已关注
			model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
		} else {
			model.addAttribute("followers", getUsersInfo(0, followerIds));
		}
		model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
		// 注意区分hostuser 和 curuser  前者是本地用户，输了用户密码的，  后者是当前网页的用户 正在查看的主页
		model.addAttribute("curUser", userService.getUser(userId));
		return "followers";
	}
	
    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }

}
