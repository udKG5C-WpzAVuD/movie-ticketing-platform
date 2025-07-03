package com.example.movieticketingplatform.service.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.dto.PageDTO;
import com.example.movieticketingplatform.model.domain.Movies;
import com.example.movieticketingplatform.mapper.MoviesMapper;
import com.example.movieticketingplatform.service.IMoviesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MoviesServiceImpl extends ServiceImpl<MoviesMapper, Movies> implements IMoviesService {


    private final MoviesMapper moviesMapper;

    public MoviesServiceImpl(MoviesMapper moviesMapper) {
        this.moviesMapper = moviesMapper;
    }

    @Override
    public Page<Movies> pageList(Movies m, PageDTO pageDTO) {
        Page<Movies> page = new Page<>(pageDTO.getPageNum(),pageDTO.getPageSize());
        page = moviesMapper.pageList(page,m);
        return page;
    }

    @Override
    public List<Movies> listAllMovies() {
        return list();
    }
}

