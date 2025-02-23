package com.chen.mapper;

import com.chen.entity.Subscription;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubscriptionMapper {

    @Insert("INSERT INTO subscription(user_id, keywords, push_notify, email_notify, status, created_time)" +
            "VALUES " +
            "(#{userId}, #{keywords}, #{pushNotify}, #{emailNotify}, #{status}, #{createdTime})")
    void insert(Subscription subscription);

    @Select("SELECT COUNT(user_id) FROM subscription WHERE user_id = #{userId} and status = 1")
    Integer countActiveByUserId(Long userId);

    @Select("SELECT * FROM subscription WHERE id = #{id}")
    Subscription findById(Long id);

    void update(Subscription subscription);

    @Select("SELECT keywords from subscription WHERE user_id = #{userId} AND status = 1")
    List<String> getAllKeywordsByUserId(Long userId);
}
