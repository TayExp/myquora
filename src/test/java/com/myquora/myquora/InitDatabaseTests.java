package com.myquora.myquora;

import java.util.Date;
import java.util.Random;

//import junit.framework.Assert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import com.myquora.myquora.dao.QuestionDAO;
import com.myquora.myquora.dao.UserDAO;
import com.myquora.myquora.model.EntityType;
import com.myquora.myquora.model.Question;
import com.myquora.myquora.model.User;
import com.myquora.myquora.service.FollowService;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql("/init-schema.sql")

public class InitDatabaseTests {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	QuestionDAO questionDAO;
	
	@Autowired
	FollowService followService;
	
	@Test
	public void contextLoads() {
		
	}
	
	@Test
	public void initDatabase() {
		Random random = new Random();
		for(int i = 0; i < 11; i++) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);
			user.setPassword("newpassword");
			userDAO.updatePassword(user);
			
			// 互相关注一下
			for(int j = 1; j < i; j++) {
				followService.follow(j, EntityType.ENTITY_USER, i);
			}
			
			Question question = new Question();
			question.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*365*5*i);
			question.setCreatedDate(date);
			question.setUserId(i+1);
			question.setTitle(String.format("TITLE{%d}", i));
			question.setContent(String.format("contentcontent %d", i));
			questionDAO.addQuestion(question);
		}
		Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
		//userDAO.deleteById(1);
		//Assert.assertNull(userDAO.selectById(1));
		System.out.println(questionDAO.selectLatestQuestions(0, 0, 10));
		System.out.println("end");
	}

}
