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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Integer getTingnum() {
        return tingnum;
    }

    public void setTingnum(Integer tingnum) {
        this.tingnum = tingnum;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
