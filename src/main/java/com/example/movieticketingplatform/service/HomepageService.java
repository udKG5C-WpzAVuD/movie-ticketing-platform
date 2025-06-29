package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.model.domain.Movie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomepageService {
    List<Movie> getFilms();

    List<Movie> getHotFilms();

    boolean updateMovieLikes(Long id, Boolean isStarClicked);

}
