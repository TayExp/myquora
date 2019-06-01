/**
 * 
 */
package com.myquora.myquora.async.handler;

import java.util.List;

import com.myquora.myquora.async.EventHandler;
import com.myquora.myquora.async.EventModel;
import com.myquora.myquora.async.EventType;

/**
 * @author miaohj
 *
 */
public class AddQuestionHandler implements EventHandler {

	/* (non-Javadoc)
	 * @see com.myquora.myquora.async.EventHandler#doHandle(com.myquora.myquora.async.EventModel)
	 */
	@Override
	public void doHandle(EventModel model) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.myquora.myquora.async.EventHandler#getSupportEventTypes()
	 */
	@Override
	public List<EventType> getSupportEventTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
