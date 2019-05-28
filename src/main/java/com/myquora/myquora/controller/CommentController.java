package com.myquora.myquora.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.myquora.myquora.model.Comment;
import com.myquora.myquora.model.EntityType;
import com.myquora.myquora.model.HostHolder;
import com.myquora.myquora.service.CommentService;
import com.myquora.myquora.service.QuestionService;
import com.myquora.myquora.service.SensitiveService;
import com.myquora.myquora.service.UserService;
import com.myquora.myquora.util.MyUtil;

@Controller
public class CommentController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	
   @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;
    
    @Autowired
    SensitiveService sensitiveService;
	
	@RequestMapping(value = "/addComment", method = {RequestMethod.POST})
	public String addComment(@RequestParam("questionId")int questionId, @RequestParam("content")String content) {
		try {
			Comment comment = new Comment();
			if(hostHolder.getUser() != null) {
				comment.setUserId(hostHolder.getUser().getId());
			} else {
				comment.setUserId(MyUtil.ANONYMOUS_USERID);
			}
			System.out.println(content);
			content = HtmlUtils.htmlEscape(content);
			comment.setContent(sensitiveService.filter(content));
			comment.setEntityId(questionId);
			System.out.println(questionService.getById(questionId).getContent());
			comment.setEntityType(EntityType.ENTITY_QUESTION);
			comment.setCreatedDate(new Date());
			comment.setStatus(0);
			
			commentService.addComment(comment);
			// 更新问题的评论数
			int commentCount = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
			questionService.updateCommentCount(comment.getEntityId(), commentCount);
			
			//异步化？
			
		} catch(Exception e) {
			logger.error("增加评论失败" + e.getMessage());
		}
		return "redirect:/question/" + String.valueOf(questionId);
	}
	
}
