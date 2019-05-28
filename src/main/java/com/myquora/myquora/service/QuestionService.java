package com.myquora.myquora.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.myquora.myquora.dao.QuestionDAO;
import com.myquora.myquora.model.Question;
@Service
public class QuestionService {
	@Autowired
	QuestionDAO questionDAO;
	
	public Question getById(int id) {
		return questionDAO.getByid(id);
	}
	
	public int addQuestion(Question question) {
		// 去除HTML格式
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		question.setContent(HtmlUtils.htmlEscape(question.getContent()));
		// 敏感词过滤
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
		return questionDAO.addQuestion(question) > 0 ? question.getId():0;
	}
	
	 public List<Question> getLatestQuestions( int userId, int offset, int limit){
		 return questionDAO.selectLatestQuestions(userId, offset, limit);
	 }

	public void updateCommentCount(int questionId, int commentCount) {
		questionDAO.updateCommentCount(questionId, commentCount);
	}
}
