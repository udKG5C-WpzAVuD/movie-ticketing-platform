package com.example.movieticketingplatform.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.movieticketingplatform.model.domain.Movies;
import com.example.movieticketingplatform.mapper.MoviesMapper;
import com.example.movieticketingplatform.model.dto.MoviesLogDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.IAdminMovieOperationLogsService;
import com.example.movieticketingplatform.model.domain.AdminMovieOperationLogs;


/**
 *
 *  前端控制器
 *
 *
 * @author lxp
 * @since 2025-06-29
 * @version v1.0
 */
@RestController
@RequestMapping("/api/adminMovieOperationLogs")
public class AdminMovieOperationLogsController {

    private final Logger logger = LoggerFactory.getLogger( AdminMovieOperationLogsController.class );

    @Autowired
    private IAdminMovieOperationLogsService adminMovieOperationLogsService;
    @Autowired
    private MoviesMapper moviesMapper;


    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse<AdminMovieOperationLogs> getById(@PathVariable("id") Long id)throws Exception {
        AdminMovieOperationLogs adminMovieOperationLogs = adminMovieOperationLogsService.getById(id);
        return JsonResponse.success(adminMovieOperationLogs);
    }
    @PostMapping("addLogs")
    public JsonResponse addLogs(@RequestBody MoviesLogDTO m){
        System.out.println("输出数据"+m.getTitle()+m.getOperationTargetType()+m.getOperationType()+m.getOperationTargetId());
       AdminMovieOperationLogs adminMovieOperationLogs = new AdminMovieOperationLogs();
       adminMovieOperationLogs.setOperationTargetId(m.getOperationTargetId());
       adminMovieOperationLogs.setOperationType(m.getOperationType());
       adminMovieOperationLogs.setOperationTargetType(m.getOperationTargetType());
        QueryWrapper<Movies> query = new QueryWrapper<>();
        query.eq("title", m.getTitle());
        Movies movie = moviesMapper.selectOne(query);
        if (movie == null) {
            return JsonResponse.failure("未找到对应的电影");
        }
        adminMovieOperationLogs.setMovieId(movie.getId());
        adminMovieOperationLogsService.save(adminMovieOperationLogs);
        return JsonResponse.success(adminMovieOperationLogs);
     }
}

