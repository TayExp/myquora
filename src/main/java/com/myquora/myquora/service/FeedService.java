package com.myquora.myquora.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myquora.myquora.dao.FeedDAO;
import com.myquora.myquora.model.Feed;

/*
 * 读取Feed
 */
@Service
public class FeedService {
	@Autowired
    FeedDAO feedDAO;
	
	public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count){
		return feedDAO.selectUserFeeds(maxId, userIds, count);
	}
	
	public boolean addFeed(Feed feed) {
		feedDAO.addFeed(feed);
		return feed.getId() > 0;
	}
	
	public Feed getById(int id) {
		return feedDAO.getFeedById(id);
	}
}
