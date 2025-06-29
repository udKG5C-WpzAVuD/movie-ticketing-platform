package com.example.movieticketingplatform.web.controller;

import com.example.movieticketingplatform.model.domain.Movie;
import com.example.movieticketingplatform.service.HomepageService;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.HomepageService;

import java.util.List;


@RestController
@RequestMapping("/api/homepage")
public class HomepageController {
    private final Logger logger = LoggerFactory.getLogger( HomepageController.class );

    @Autowired
    private HomepageService homepageService;

    @GetMapping("filmList")
    public JsonResponse filmList() throws Exception{
        List<Movie> movies = homepageService.getFilms();
        return JsonResponse.success(movies);
    }

    @GetMapping("hotList")
    public JsonResponse hotList() throws Exception{
        List<Movie> hotMovies = homepageService.getHotFilms();
        return JsonResponse.success(hotMovies);
    }

    @Data
    public static class UpdateLikesRequest {
        private Long id;
        private Boolean isStarClicked;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Boolean getIsStarClicked() {
            return isStarClicked;
        }

        public void setIsStarClicked(Boolean starClicked) {
            isStarClicked = starClicked;
        }
    }
    @PatchMapping("updateLikes")
    public JsonResponse updateLikes(@RequestBody UpdateLikesRequest request) {
        if (request.getId() == null) {
            return JsonResponse.failure("电影ID不能为空");
        }
        boolean success = homepageService.updateMovieLikes(request.getId(), request.getIsStarClicked());
        return success ? JsonResponse.success("更新成功") : JsonResponse.failure("更新失败");
    }
}
