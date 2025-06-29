package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.model.domain.Likes;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 想看表 服务类
 * </p>
 *
 * @author lxp
 * @since 2025-06-28
 */
public interface ILikesService extends IService<Likes> {

    boolean addLike(int uid, int mid);

    boolean deleteLike(int uid, int mid);

    boolean getLike(int uid, int mid);
}
