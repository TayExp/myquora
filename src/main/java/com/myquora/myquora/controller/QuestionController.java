package com.myquora.myquora.controller;

import java.util.Date;

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
import com.myquora.myquora.model.Question;
import com.myquora.myquora.service.QuestionService;
import com.myquora.myquora.service.UserService;
import com.myquora.myquora.util.MyUtil;

@Controller
public class QuestionController {

	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
   @Autowired
    QuestionService questionService;

//    @Autowired
//    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;
	
	@RequestMapping(value = "/question/add", method = {RequestMethod.POST})
	@ResponseBody
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
				return MyUtil.getJSONString(0);
			}
		} catch(Exception e) {
			logger.error("增加题目失败" + e.getMessage());
		}
		return MyUtil.getJSONString(1,"失败");
	}
	
	@RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
	public String questionDetail(Model model, @PathVariable("qid") int qid) {
		Question question = questionService.getById(qid);
		model.addAttribute("question", question);
		
		return "detail";
	}
}
