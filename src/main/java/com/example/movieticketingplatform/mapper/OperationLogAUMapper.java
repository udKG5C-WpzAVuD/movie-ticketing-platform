package com.example.movieticketingplatform.mapper;

import com.example.movieticketingplatform.model.domain.OperationLogAU;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogAUMapper {
    @Insert("INSERT INTO admin_user_operation_logs(user_id, operation_type, operation_target_type, operation_target_id, operation_time) " +
            "VALUES(#{userId}, #{operationType}, #{operationTargetType}, #{operationTargetId}, #{operationTime})")
    int insert(OperationLogAU log);  // 修改返回值为int，表示影响的行数
}
