package com.example.movieticketingplatform.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin_user_operation_logs")  // 指定正确的表名
public class OperationLogAU {

    @TableId(value = "admin_log_id", type = IdType.AUTO)  // 指定主键字段名和自增策略
    private Long adminLogId;

    @TableField("user_id")// 数据库列名与属性名不一致，使用@TableField注解
    private Long userId;

    @TableField("operation_type")
    private String operationType;

    @TableField("operation_target_type")
    private String operationTargetType;

    @TableField("operation_target_id")
    private Long operationTargetId;

    @TableField("operation_time")
    private Timestamp operationTime;

    public Long getAdminLogId() {
        return adminLogId;
    }

    public void setAdminLogId(Long adminLogId) {
        this.adminLogId = adminLogId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationTargetType() {
        return operationTargetType;
    }

    public void setOperationTargetType(String operationTargetType) {
        this.operationTargetType = operationTargetType;
    }

    public Long getOperationTargetId() {
        return operationTargetId;
    }

    public void setOperationTargetId(Long operationTargetId) {
        this.operationTargetId = operationTargetId;
    }

    public Timestamp getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Timestamp operationTime) {
        this.operationTime = operationTime;
    }
}
