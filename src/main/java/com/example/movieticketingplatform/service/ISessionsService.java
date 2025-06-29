package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.model.domain.Sessions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 场次表 服务类
 * </p>
 *
 * @author lxp
 * @since 2025-06-28
 */
public interface ISessionsService extends IService<Sessions> {

    int deleteSession(Sessions session);
}
