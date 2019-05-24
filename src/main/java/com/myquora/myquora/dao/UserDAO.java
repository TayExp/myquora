package com.myquora.myquora.dao;

import org.apache.ibatis.annotations.*;

import com.myquora.myquora.model.User;

@Mapper //指定和mybatis关联
public interface UserDAO {
	// 公共的部分， 注意有空格
	String TABLE_NAME = " user ";
	String INSERT_FIELDS = " name, password, salt, head_url ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;
	@Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELDS, 
		") values (#{name},#{password},#{salt},#{headUrl})"}) // 读取的是user里的字段
	int addUser(User user);
	
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where id=#{id}"})
	User selectById(int id);
	
	@Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
	void updatePassword(User user);
	
	@Delete({"delete from ",TABLE_NAME, "where id=#{id}"})
	void deleteById(int id);

	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where name=#{name}"})
	User selectByName(String name);
}
