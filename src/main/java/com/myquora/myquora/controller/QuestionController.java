package com.myquora.myquora.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.myquora.myquora.service.LikeService;
import com.myquora.myquora.service.QuestionService;
import com.myquora.myquora.service.UserService;
import com.myquora.myquora.util.MyUtil;

@Controller
public class QuestionController {

	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;
    
    @Autowired
    LikeService likeService;
    
    @Autowired
    FollowService followService;
    
    @Autowired
    EventProducer eventProducer;
	
	@RequestMapping(value = "/question/add", method = {RequestMethod.POST})
	@ResponseBody  //返回json串的时候要写response注解
	public String addQuestion(@RequestParam("title")String title, @RequestParam("content")String content) {
		try {
			Question question = new Question();
			question.setTitle(title);
			question.setContent(content);
			question.setCreatedDate(new Date());
			if(hostHolder.getUser() == null) {
				question.setUserId(MyUtil.ANONYMOUS_USERID);
			} else {
				question.setUserId(hostHolder.getUser().getId());
			}
			if(questionService.addQuestion(question) > 0) {
				eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                        .setActorId(question.getUserId()).setEntityId(question.getId())
                .setExt("title", question.getTitle()).setExt("content", question.getContent()));

				return MyUtil.getJSONString(0);
			}
		} catch(Exception e) {
			logger.error("增加题目失败" + e.getMessage());
		}
		return MyUtil.getJSONString(1,"失败");
	}
	
	@RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET, RequestMethod.POST})
	public String questionDetail(Model model, @PathVariable("qid") int qid) {
		Question question = questionService.getById(qid);
		model.addAttribute("question", question);
		List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> vos = new ArrayList<>();
		for(Comment comment : commentList) {
			ViewObject vo = new ViewObject();
			vo.set("comment", comment);
			vo.set("user", userService.getUser(comment.getUserId()));
			if(hostHolder.getUser() == null) {
				vo.set("liked", 0);
			} else {
				vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
			}
			vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
			vos.add(vo);
		}
		model.addAttribute("comments", vos);
        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }
		return "detail";
	}
}
