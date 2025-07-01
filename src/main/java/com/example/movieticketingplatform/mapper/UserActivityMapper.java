package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movieticketingplatform.model.domain.UserActivity;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxp
 * @since 2025-06-30
 */
public interface UserActivityMapper extends BaseMapper<UserActivity> {

    List<UserActivity> selectUserActivity();
}
