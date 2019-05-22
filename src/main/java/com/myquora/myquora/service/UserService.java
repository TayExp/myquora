package com.myquora.myquora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myquora.myquora.dao.UserDAO;
import com.myquora.myquora.model.User;
@Service
public class UserService {
	 @Autowired
	 UserDAO userDAO;

     public User getUser(int id) {
        return userDAO.selectById(id);
     } 
}
