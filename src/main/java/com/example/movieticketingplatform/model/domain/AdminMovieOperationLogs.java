package com.example.movieticketingplatform.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxp
 * @since 2025-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin_movie_operation_logs")
@ApiModel(value="AdminMovieOperationLogs对象", description="")
public class AdminMovieOperationLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "admin_log_id", type = IdType.AUTO)
    private Integer adminLogId;

    @TableField("movie_id")
    private Long movieId;

    @TableField("operation_type")
    private String operationType;

    @TableField("operation_target_type")
    private String operationTargetType;

    @TableField("operation_target_id")
    private Long operationTargetId;

    @TableField("operation_time")
    private LocalDateTime operationTime;


}
