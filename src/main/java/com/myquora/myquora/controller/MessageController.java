package com.myquora.myquora.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myquora.myquora.model.HostHolder;
import com.myquora.myquora.model.Message;
import com.myquora.myquora.model.Question;
import com.myquora.myquora.model.User;
import com.myquora.myquora.model.ViewObject;
import com.myquora.myquora.service.MessageService;
import com.myquora.myquora.service.QuestionService;
import com.myquora.myquora.service.UserService;
import com.myquora.myquora.util.MyUtil;

@Controller
public class MessageController {

	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	
    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;
	
    @Autowired
    QuestionService questionService;
    
	@RequestMapping(value = "/msg/addMessage", method = {RequestMethod.POST})
	@ResponseBody
	public String addMessage(@RequestParam("toName") String toName,
							  @RequestParam("content") String content) {
		try {
			if(hostHolder.getUser() == null) {
				return MyUtil.getJSONString(999, "未登录");
			}
			User user = userService.selectByName(toName);
			if(user == null) {
				return MyUtil.getJSONString(1, "用户不存在");
			}
			Message msg = new Message();
			msg.setContent(content);
			int fromId = hostHolder.getUser().getId();
			int toId = user.getId();
			msg.setFromId(fromId);
			msg.setToId(toId);
			msg.setCreatedDate(new Date());
			msg.setConversationId(String.format("%d_%d", Math.min(fromId, toId), Math.max(fromId, toId)));
			messageService.addMessage(msg);
			return MyUtil.getJSONString(0);//responsebody时
		} catch(Exception e) {
			logger.error("增加站内信失败" + e.getMessage());
			return MyUtil.getJSONString(1,"插入站内信失败");
		}
		
	}
    
	@RequestMapping(value = "/msg/jsonAddMessage", method = {RequestMethod.POST})
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId,
							  @RequestParam("toId") int toId,
							  @RequestParam("content") String content) {
		try {
			Message msg = new Message();
			msg.setContent(content);
			msg.setFromId(fromId);
			msg.setToId(toId);
			msg.setCreatedDate(new Date());
			msg.setConversationId(String.format("%d_%d", Math.min(fromId, toId), Math.max(fromId, toId)));
			messageService.addMessage(msg);
			return MyUtil.getJSONString(0);//responsebody时
		} catch(Exception e) {
			logger.error("增加评论失败" + e.getMessage());
			return MyUtil.getJSONString(1,"插入评论失败");
		}
		
	}

	
	//站内信列表，某个用户的列表
	@RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model) {
		try {
			int localUserId = hostHolder.getUser().getId();
			List<ViewObject> conversations = new ArrayList<>();
			List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
			System.out.println(conversationList);
			for(Message msg : conversationList) {
				ViewObject vo = new ViewObject();
				vo.set("conversation", msg);
				int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId(); //是收的还是会回的
				User user = userService.getUser(targetId);
				vo.set("user", user); //发消息的用户
				vo.set("unread", messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
				conversations.add(vo);
			}
			model.addAttribute("conversations", conversations);
		} catch(Exception e) {
			logger.error("获取站内信列表失败" + e.getMessage());
		}
		return "letter";
	}
	
	//一个会话的具体消息详情，user是发送人
	@RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
	public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
		try {
			List<ViewObject> messages = new ArrayList<>();
			List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
			for(Message msg : conversationList) {
				ViewObject vo = new ViewObject();
				vo.set("message", msg); //展示这个会话的具体消息
				User user = userService.getUser(msg.getFromId());
				if(user == null) {
					continue;
				}
				vo.set("headUrl", user.getHeadUrl());
				vo.set("userId", user.getId());
				messages.add(vo);
			}
			model.addAttribute("messages", messages);
			} catch(Exception e) {
				logger.error("获取消息详情失败" + e.getMessage());
			}
		 return "letterDetail";
	 }
}
