package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.model.domain.Sessions;
import com.example.movieticketingplatform.mapper.SessionsMapper;
import com.example.movieticketingplatform.service.ISessionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 场次表 服务实现类
 * </p>
 *
 * @author lxp
 * @since 2025-06-28
 */
@Service
public class SessionsServiceImpl extends ServiceImpl<SessionsMapper, Sessions> implements ISessionsService {


    private final SessionsMapper sessionsMapper;

    public SessionsServiceImpl(SessionsMapper sessionsMapper) {
        this.sessionsMapper = sessionsMapper;
    }

    @Override
    public int deleteSession(Sessions session) {
        if (session == null) {
            return 0;
        }

        // 执行删除
        System.out.println("删除数据" + session.getId());
        return sessionsMapper.deleteById(session.getId());
    }
}
