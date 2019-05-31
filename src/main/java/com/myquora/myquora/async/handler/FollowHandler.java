package com.myquora.myquora.async.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myquora.myquora.async.EventHandler;
import com.myquora.myquora.async.EventModel;
import com.myquora.myquora.async.EventType;
import com.myquora.myquora.model.EntityType;
import com.myquora.myquora.model.Message;
import com.myquora.myquora.service.MessageService;
import com.myquora.myquora.service.UserService;
import com.myquora.myquora.util.MyUtil;

@Component
public class FollowHandler implements EventHandler{
	@Autowired
	MessageService messageService;
	@Autowired
	UserService userService;
	
	@Override
	public void doHandle(EventModel model) {
		Message message = new Message();
		message.setFromId(MyUtil.SYSTEM_USERID);
		message.setToId(model.getEntityOwnerId());
		message.setCreatedDate(new Date());
		message.setConversationId(MyUtil.SYSTEM_USERID+"_"+message.getToId());
		if(model.getEntityType() == EntityType.ENTITY_QUESTION) {
			message.setContent("用户" + userService.getUser(model.getActorId()).getName()
					+ "关注了你的问题，http://127.0.0.1:8080/question/"+model.getEntityId());
		} else if(model.getEntityType() == EntityType.ENTITY_USER) {
			message.setContent("用户" + userService.getUser(model.getActorId()).getName()
					+ "关注了你，http://127.0.0.1:8080/user/"+model.getActorId());
		}
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.FOLLOW);
	}
}
