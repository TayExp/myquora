package com.myquora.myquora.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myquora.myquora.dao.CommentDAO;
import com.myquora.myquora.model.Comment;

@Service
public class CommentService {

	@Autowired
	private CommentDAO commentDAO;
	
	public List<Comment> getCommentByEntity(int entityId, int entityType) {
		return commentDAO.selectByEntity(entityId, entityType);
	} 
	
	public int addComment(Comment comment) {
		return commentDAO.addComment(comment);
	}
	
	public int getCommentCount(int entityId, int entityType) {
		return commentDAO.getCommentCount(entityId, entityType);
	}
	
	public void deleteComment(int entityId, int entityType) {
		commentDAO.updateStatus(entityId, entityType, 1);
	}
	
}
