package com.example.movieticketingplatform.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @since 2025-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_activity")
@ApiModel(value="UserActivity对象", description="")
public class UserActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

        @ApiModelProperty(value = "用户ID（外键，关联user表）")
    @TableField("user_id")
    private Long userId;

        @ApiModelProperty(value = "登录次数")
    @TableField("login_count")
    private Integer loginCount;

        @ApiModelProperty(value = "购票次数")
    @TableField("purchase_count")
    private Integer purchaseCount;

        @ApiModelProperty(value = "总消费金额")
    @TableField("total_spent")
    private BigDecimal totalSpent;

        @ApiModelProperty(value = "最后活跃时间")
    @TableField("last_active_time")
    private LocalDateTime lastActiveTime;

        @ApiModelProperty(value = "创建时间")
    @TableField("gmt_created")
    private LocalDateTime gmtCreated;

        @ApiModelProperty(value = "更新时间")
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;


}
