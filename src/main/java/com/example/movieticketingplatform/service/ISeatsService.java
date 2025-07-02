package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.model.domain.Seats;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxp
 * @since 2025-06-28
 */
public interface ISeatsService extends IService<Seats> {

    List<Seats> findBySessionId(Long id);

    Seats getBySessionandCode(Long sessionId, String code);
}
