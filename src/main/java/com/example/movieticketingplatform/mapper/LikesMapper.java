package com.example.movieticketingplatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movieticketingplatform.model.domain.Likes;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LikesMapper extends BaseMapper<Likes> {


    int insertLike(@Param("uid") int uid, @Param("mid") int mid, @Param("date") Date date);

    int deleteLike(@Param("uid") int uid, @Param("mid") int mid);

    boolean existsLike(@Param("uid") int uid, @Param("mid") int mid);
}
