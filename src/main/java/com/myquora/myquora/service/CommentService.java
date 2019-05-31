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
	
	public Comment getCommentById(int id) {
        return commentDAO.getCommentById(id);
    }
	
	public int addComment(Comment comment) {
		return commentDAO.addComment(comment);
	}
	
	public int getCommentCount(int entityId, int entityType) {
		return commentDAO.getCommentCount(entityId, entityType);
	}
	
	public boolean deleteComment(int commentId) {
        return commentDAO.updateStatus(commentId, 1) > 0;
    }

	public int getUserCommentCount(int userId) {
		return commentDAO.getUserCommentCount(userId);
	}
	
}
