package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.mapper.SeatsMapper;
import com.example.movieticketingplatform.model.domain.Seats;
import com.example.movieticketingplatform.service.ISeatsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxp
 * @since 2025-06-28
 */
@Service
public class SeatsServiceImpl extends ServiceImpl<SeatsMapper, Seats> implements ISeatsService {

    @Override
    public List<Seats> findBySessionId(Long sessionId) {
        // 调用baseMapper执行自定义SQL
        return baseMapper.findBySessionId(sessionId);
    }

    @Override
    public Seats getBySessionandCode(Long sessionId, String code) {
        return baseMapper.getBySessionandCode(sessionId,code);
    }
}
