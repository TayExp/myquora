package com.myquora.myquora.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myquora.myquora.dao.QuestionDAO;
import com.myquora.myquora.model.Question;
@Service
public class QuestionService {
	@Autowired
	QuestionDAO questionDAO;
	
	 public List<Question> getLatestQuestions( int userId, int offset, int limit){
		 return questionDAO.selectLatestQuestions(userId, offset, limit);
	 }
}
