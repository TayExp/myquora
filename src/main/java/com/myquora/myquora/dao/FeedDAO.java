package com.myquora.myquora.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.myquora.myquora.model.Comment;
import com.myquora.myquora.model.Feed;

@Mapper
public interface FeedDAO {
	String TABLE_NAME = " feed ";
	String INSERT_FIELDS = " user_id, data, created_date, type ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;
	
	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
		") values (#{userId},#{data},#{createdDate},#{type})"})
	int addFeed(Feed feed);
	
	/**
	 * 推，直接取
	 * @param 本地用户id
	 * @return
	 */
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
	Feed getFeedById(int id);
	
	/**
	 * 拉的模式,有配置文件
	 * @param maxId 采用增量取方式，不全一次取，设置本次最大userId
	 * @param userIds 未登录 所有的,不需要userIds
	 * @param count 分页
	 * @return
	 */
	List<Feed> selectUserFeeds(@Param("maxId") int maxId, 
							   @Param("userIds") List<Integer> userIds,
							   @Param("count") int count);

}
