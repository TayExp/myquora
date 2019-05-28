package com.myquora.myquora.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myquora.myquora.dao.CommentDAO;
import com.myquora.myquora.dao.MessageDAO;
import com.myquora.myquora.model.Comment;
import com.myquora.myquora.model.Message;

@Service
public class MessageService {

	@Autowired
	private MessageDAO messageDAO;
	
	public int addMessage(Message message) {
		return messageDAO.addMessage(message);
	}
	
	public List<Message> getConversationDetail(String conversationId, int offset, int limit){
		return messageDAO.getConversationDetail(conversationId, offset, limit);
	}
	
	public List<Message> getConversationList(int userId, int offset, int limit){
		return messageDAO.getConversationList(userId, offset, limit);
	}
	
	public int getConversationUnreadCount(int userId, String conversationId) {
		return messageDAO.getConvesationUnreadCount(userId, conversationId);
	}
}
