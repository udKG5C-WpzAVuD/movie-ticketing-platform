package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movieticketingplatform.model.domain.Movie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomepageMapper extends BaseMapper<Movie> {
    List<Movie> selectMovies();

    List<Movie> selectHot();

    int updateCountById(@Param("id") Long id, @Param("newCount") int newCount);
}