package com.example.movieticketingplatform.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.movieticketingplatform.model.dto.PageDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IMoviesService;
import com.example.movieticketingplatform.model.domain.Movies;


/**
 *
 *  前端控制器
 *
 *
 * @author lxp
 * @since 2025-06-25
 * @version v1.0
 */
@RestController
@RequestMapping("/api/movies")
public class MoviesController {

    private final Logger logger = LoggerFactory.getLogger( MoviesController.class );

    @Autowired
    private IMoviesService moviesService;


    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<Movies> getById(@PathVariable("id") Long id)throws Exception {
        Movies movies = moviesService.getById(id);
        return JsonResponse.success(movies);
    }
    //新增分页查询
    @GetMapping("moviePageList")
    public JsonResponse moviePageList(Movies m, PageDTO pageDTO) throws Exception {

//        if(pageDTO==null){
//            pageDTO=new PageDTO();
//            pageDTO.setPageSize(10);
//            pageDTO.setPageNum(1);
//
//        }

Page<Movies> movies = moviesService.pageList(m,pageDTO);
        System.out.println(pageDTO.getPageNum()+" "+pageDTO.getPageSize());
return JsonResponse.success(movies);

    }

    //添加
    @PostMapping("savemovie")
    public JsonResponse save(@RequestBody Movies m)throws Exception {
        //验证 用户是否已存在
        // 1. 验证电影是否已存在（根据title判断）
        LambdaQueryWrapper<Movies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Movies::getTitle, m.getTitle());
        long count = moviesService.count(queryWrapper);
        if (count > 0) {
            // 如果电影已存在，返回错误信息
            return JsonResponse.failure("已有重复");
        }
        boolean save = moviesService.save(m);
        return JsonResponse.success(save);
    }


    //修改
    @PutMapping("updateMovie")
    public JsonResponse update(@RequestBody Movies m)throws Exception {
        System.out.println("看这儿："+m.getTitle()+m.getIsDeleted()+m.getIsPutaway());
        boolean update = moviesService.updateById(m);
        return JsonResponse.success(update);
    }
    @GetMapping("getMoviesid")
    public JsonResponse getMoviestitle(@RequestParam Long id)throws Exception {
        Movies movies= moviesService.getById(id);
        return JsonResponse.success(movies);
    }
}

