package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movieticketingplatform.model.domain.Seats;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxp
 * @since 2025-06-28
 */
public interface SeatsMapper extends BaseMapper<Seats> {
    List<Seats> findBySessionId(@Param("sessionId") Long sessionId);
}
