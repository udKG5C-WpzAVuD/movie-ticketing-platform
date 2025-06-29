package com.example.movieticketingplatform.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.movieticketingplatform.mapper.MoviesMapper;
import com.example.movieticketingplatform.model.domain.Movies;
import com.example.movieticketingplatform.model.dto.SessionDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.movieticketingplatform.common.JsonResponse;
import com.example.movieticketingplatform.service.ISessionsService;
import com.example.movieticketingplatform.model.domain.Sessions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 *
 *  前端控制器
 *
 *
 * @author lxp
 * @since 2025-06-28
 * @version v1.0
 */
@RestController
@RequestMapping("/api/sessions")
public class SessionsController {

    private final Logger logger = LoggerFactory.getLogger( SessionsController.class );

    @Autowired
    private ISessionsService sessionsService;
    @Autowired
    private MoviesMapper moviesMapper;


    /**
    * 描述：根据Id 查询
    *
    */
    @GetMapping(value = "/get/{id}")
    @ResponseBody
    public JsonResponse<Sessions> getById(@PathVariable("id") Long id)throws Exception {
        Sessions sessions = sessionsService.getById(id);
        return JsonResponse.success(sessions);
    }
    @GetMapping("getSessions")
    public JsonResponse<List<SessionDTO>> getSessions() throws Exception {
        try {
            // 查询所有排片记录
            List<Sessions> sessionsList = sessionsService.list();

            // 转换为DTO列表
            List<SessionDTO> dtoList = new ArrayList<>();

            for (Sessions session : sessionsList) {
                // 查询关联的电影信息
                Movies movie = moviesMapper.selectById(session.getMovieId());
                if (movie == null) {
                    continue; // 或者可以记录日志
                }

                // 构建DTO对象
                SessionDTO dto = new SessionDTO();
                dto.setTingnum(session.getTingnum());
                dto.setTime(session.getTime());
                dto.setTitle(movie.getTitle());
                dto.setSid(session.getId());

                dtoList.add(dto);
            }

            return JsonResponse.success(dtoList);
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("获取排片列表失败: " + e.getMessage());
            e.printStackTrace();
            return JsonResponse.failure("获取排片列表失败: " + e.getMessage());
        }
    }
    @PostMapping("addsessions")
    public JsonResponse addSession(@RequestBody SessionDTO dto) {
        System.out.println("我进来啦红红火火恍恍惚惚");
        System.out.println(dto.getTime());
        System.out.println(dto.getTingnum()+dto.getTitle());
        QueryWrapper<Movies> query = new QueryWrapper<>();
        query.eq("title", dto.getTitle());
        Movies movie = moviesMapper.selectOne(query);
        if (movie == null) {
            return JsonResponse.failure("未找到对应的电影");
        }

        Sessions session = new Sessions();
        session.setMovieId(movie.getId());
        session.setTingnum(dto.getTingnum());
        session.setTime(dto.getTime());
        session.setCreateTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());
        sessionsService.save(session);

        return JsonResponse.success(session);
    }
@PostMapping("deletescreen")
    public JsonResponse deleteScreen(@RequestBody SessionDTO dto) {
    System.out.println("完整接收到的DTO: " + dto);
    System.out.println("数据在这儿"+dto.getTingnum());

    QueryWrapper<Sessions> query = new QueryWrapper<>();
    query.eq("id", dto.getSid());
    Sessions session = sessionsService.getOne(query);
    System.out.println("数据在这儿"+dto.getTingnum());
    System.out.println("删除数据" + session.getId());
    if (session == null) {
        return JsonResponse.failure("未找到对应的电影");
    }
    int deleted = sessionsService.deleteSession(session);
    if (deleted==1) {
        return JsonResponse.success("排片删除成功");
    } else {
        return JsonResponse.failure("未找到该排片记录");
    }
}
}


