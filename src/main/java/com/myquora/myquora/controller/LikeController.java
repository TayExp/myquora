package com.myquora.myquora.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myquora.myquora.async.EventModel;
import com.myquora.myquora.async.EventProducer;
import com.myquora.myquora.async.EventType;
import com.myquora.myquora.model.Comment;
import com.myquora.myquora.model.EntityType;
import com.myquora.myquora.model.HostHolder;
import com.myquora.myquora.service.CommentService;
import com.myquora.myquora.service.LikeService;
import com.myquora.myquora.service.UserService;
import com.myquora.myquora.util.MyUtil;

@Controller
public class LikeController {

	private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
	@Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;
    
    @Autowired
    LikeService likeService;
    
    @Autowired
    EventProducer eventProducer;
	
	@RequestMapping(value = "/like", method = {RequestMethod.POST})
	@ResponseBody
	public String like(@RequestParam("commentId")int commentId) {
		if(hostHolder.getUser() == null) {
			return MyUtil.getJSONString(999);//未登录
		}
		Comment comment = commentService.getCommentById(commentId);
	 	//异步：// return this 可链式set
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));
		long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		return MyUtil.getJSONString(0, String.valueOf(likeCount));
	}

	@RequestMapping(value = "/dislike", method = {RequestMethod.POST})
	@ResponseBody
	public String dislike(@RequestParam("commentId")int commentId) {
		if(hostHolder.getUser() == null) {
			return MyUtil.getJSONString(999);//未登录
		}
		long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		return MyUtil.getJSONString(0, String.valueOf(likeCount));
	}
}
