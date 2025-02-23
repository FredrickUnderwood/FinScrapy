package com.chen.mapper;

import com.chen.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    List<User> findByCreateInfo(User user);

    @Insert("INSERT INTO user(username, password, level, email, phone, created_time, status)" +
            "VALUES" +
            "(#{username}, #{password}, #{level}, #{email}, #{phone}, #{createdTime}, #{status})")
    void insert(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
}
