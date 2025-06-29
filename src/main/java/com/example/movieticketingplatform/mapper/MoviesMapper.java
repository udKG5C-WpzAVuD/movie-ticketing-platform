package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.domain.Movies;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 电影信息表 Mapper 接口
 * </p>
 *
 * @author lxp
 * @since 2025-06-25
 */
@Mapper
public interface MoviesMapper extends BaseMapper<Movies> {
    Page<Movies> pageList(@Param("page")Page<Movies> page, @Param("movies") Movies movies);

}
