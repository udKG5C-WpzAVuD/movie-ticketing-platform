package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {

    Page<User> pageList(@Param("page") Page<User> page, @Param("user") User user);

    List<User> selectUsers();
}
