package com.myquora.myquora.dao;

import org.apache.ibatis.annotations.*;

import com.myquora.myquora.model.LoginTicket;
import com.myquora.myquora.model.User;

@Mapper
public interface LoginTicketDAO {
	String TABLE_NAME = " login_ticket ";
	String INSERT_FIELDS = " user_id, expired, status, ticket ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;
	
	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
		") values (#{userId},#{expired},#{status},#{ticket})" })
	int addTicket(LoginTicket ticket);
	
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where ticket=#{ticket}"})
	LoginTicket selectByTicket(String ticket);
	
	//用户登出 把status改一下表示无效了
	@Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
	void updateStatus(@Param("ticket") String ticket, @Param("status")int status);
	
	
}
