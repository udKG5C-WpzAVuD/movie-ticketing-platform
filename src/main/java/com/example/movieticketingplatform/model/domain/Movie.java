package com.example.movieticketingplatform.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("movies")
@ApiModel(value="Movie对象", description="电影信息表")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "电影名称")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "导演")
    @TableField("director")
    private String director;

    @ApiModelProperty(value = "主演")
    @TableField("actors")
    private String actors;

    @ApiModelProperty(value = "类型/分类")
    @TableField("genre")
    private String genre;

    @ApiModelProperty(value = "时长(分钟)")
    @TableField("duration")
    private Integer duration;

    @ApiModelProperty(value = "上映日期")
    @TableField("release_date")
    private LocalDateTime releaseDate;

    @ApiModelProperty(value = "语言")
    @TableField("language")
    private String language;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "海报URL")
    @TableField("poster_url")
    private String posterUrl;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty(value = "是否上架")
    @TableField("is_putaway")
    private Boolean isPutaway;

    @ApiModelProperty(value = "联系电话")
    @TableField("contact_phone")
    private String contactPhone;

    @ApiModelProperty(value = "想看数量")
    @TableField("count")
    private Integer count;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "价格")
    @TableField("price")
    private Integer price;
    public Integer getCount() {
        return count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getPutaway() {
        return isPutaway;
    }

    public void setPutaway(Boolean putaway) {
        isPutaway = putaway;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setCount(Integer count) {
        this.count = count;
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

