package com.myquora.myquora.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.myquora.myquora.model.Message;

@Mapper
public interface MessageDAO {
	String TABLE_NAME = " message ";
	String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;
	
	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
		") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
	int addMessage(Message message);
	
	@Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME, 
		"where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
	List<Message> getConversationDetail(@Param("conversationId")String conversationId,@Param("offset") int offset, @Param("limit") int limit);

	//某个用户的消息列表及会话个数
	@Select({"select ", INSERT_FIELDS, " , count(id) as id from ",
		"(select * from ", TABLE_NAME, "where from_id=#{userId} or to_id=#{userId} order by id desc) tt",
		" group by conversation_id order by created_date limit #{offset}, #{limit}"})
	List<Message> getConversationList(@Param("userId") int userId,@Param("offset") int offset, @Param("limit") int limit);

	//某个用户的未读消息数
	@Select({"select count(id) from ", TABLE_NAME, "where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
	int getConvesationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
