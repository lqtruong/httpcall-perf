package com.turong.training.polling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turong.training.polling.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from users where id = #{id}")
    Optional<User> findUser(final String id);

    @Insert("insert into users(username,email) values(#{username},#{email})")
    int insert(final User userToCreate);

    @Select("select * from users where email = #{email}")
    Optional<User> findUserByEmail(final String email);

    @Delete("delete from users where email = #{email}")
    int deleteByEmail(final String email);

    @Delete("delete from users where id = #{id}")
    int deleteById(String id);

    int insertBatch(@Param("users") List<User> usersToCreate);

    int updateBatch(@Param("users") List<User> usersToUpdate);

}
