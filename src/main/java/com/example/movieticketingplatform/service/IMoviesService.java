package com.example.movieticketingplatform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.dto.PageDTO;
import com.example.movieticketingplatform.model.domain.Movies;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 电影信息表 服务类
 * </p>
 *
 * @author lxp
 * @since 2025-06-25
 */
public interface IMoviesService extends IService<Movies> {




    Page<Movies> pageList(Movies m, PageDTO pageDTO);

    List<Movies> listAllMovies();
}
