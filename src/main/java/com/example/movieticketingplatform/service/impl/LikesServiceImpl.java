package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.model.domain.Likes;
import com.example.movieticketingplatform.mapper.LikesMapper;
import com.example.movieticketingplatform.service.ILikesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements ILikesService {
    @Autowired
    LikesMapper likesMapper;
    @Override
    public boolean addLike(int uid, int mid) {
        int Uid = uid;
        int Mid = mid;
        Date date = new Date();
        // 使用自定义 SQL（需在 Mapper 中定义）
        return likesMapper.insertLike(Uid, Mid, date) > 0;
    }

    @Override
    public boolean deleteLike(int uid, int mid) {
        int Uid = uid;
        int Mid = mid;
        return likesMapper.deleteLike(Uid, Mid) > 0;
    }

    @Override
    public boolean getLike(int uid, int mid) {
        return likesMapper.existsLike(uid, mid);
    }
}
