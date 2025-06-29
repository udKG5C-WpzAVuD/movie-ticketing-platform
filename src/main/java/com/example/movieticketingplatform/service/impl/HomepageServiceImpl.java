package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.mapper.HomepageMapper;
import com.example.movieticketingplatform.model.domain.Movie;
import com.example.movieticketingplatform.service.HomepageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomepageServiceImpl implements HomepageService {
    @Autowired
    HomepageMapper homepageMapper;

    @Override
    public List<Movie> getFilms() {
        List<Movie> movies = homepageMapper.selectMovies();
        return movies != null ? movies : new ArrayList<>();
    }

    @Override
    public List<Movie> getHotFilms() {
        List<Movie> movies = homepageMapper.selectHot();
        return movies != null ? movies : new ArrayList<>();
    }

    @Override
    public boolean updateMovieLikes(Long id, Boolean isStarClicked) {
        Movie movie = homepageMapper.selectById(id);
        int delta = Boolean.TRUE.equals(isStarClicked) ? 1 : -1;
        int newCount = movie.getCount() + delta;
        int updatedRows = homepageMapper.updateCountById(id, newCount);
        return updatedRows > 0;
    }
}
