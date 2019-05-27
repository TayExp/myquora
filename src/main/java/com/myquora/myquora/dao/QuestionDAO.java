package com.myquora.myquora.dao;

import java.util.List;
import org.apache.ibatis.annotations.*;
import com.myquora.myquora.model.Question;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"Select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Question getByid(int id);

    @Update({"update ", TABLE_NAME, "set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id")int id, @Param("commentCount") int commentCount);
    
    
    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);
}
