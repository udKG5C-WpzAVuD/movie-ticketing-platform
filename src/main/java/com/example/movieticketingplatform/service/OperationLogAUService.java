package com.example.movieticketingplatform.service;

import com.example.movieticketingplatform.mapper.OperationLogAUMapper;
import com.example.movieticketingplatform.model.domain.OperationLogAU;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
public class OperationLogAUService {
    private final OperationLogAUMapper operationLogAUMapper;

    public OperationLogAUService(OperationLogAUMapper operationLogAUMapper) {
        this.operationLogAUMapper = operationLogAUMapper;
    }

    public void logOperation(Long userId, String operationType,
                             String operationTargetType, Long operationTargetId) {
        OperationLogAU log = new OperationLogAU();
        log.setUserId(userId);
        log.setOperationType(operationType);
        log.setOperationTargetType(operationTargetType);
        log.setOperationTargetId(operationTargetId);
        log.setOperationTime(new Timestamp(System.currentTimeMillis()));

        try {
            int result = operationLogAUMapper.insert(log);
            if(result <= 0) {
                throw new RuntimeException("操作日志插入失败，影响行数为0");
            }
        } catch (Exception e) {
            throw new RuntimeException("记录操作日志失败", e);
        }
    }
}
