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
 * 场次表
 * </p>
 *
 * @author lxp
 * @since 2025-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sessions")
@ApiModel(value="Sessions对象", description="场次表")
public class Sessions implements Serializable {

    private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "场次表Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

        @ApiModelProperty(value = "电影id")
    @TableField("movie_id")
    private Long movieId;

        @ApiModelProperty(value = "厅号")
    @TableField("tingnum")
    private Integer tingnum;

        @ApiModelProperty(value = "时间段")
    @TableField("time")
    private LocalDateTime time;

        @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

        @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "是否删除")
    @TableField("is_deleted")
    private Boolean isDeleted;

}
