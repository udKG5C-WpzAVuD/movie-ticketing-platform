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
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("users")
@ApiModel(value="User对象", description="用户信息表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "电子邮件")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "注册时间")
    @TableField("registration_time")
    private LocalDateTime registrationTime;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private String status;  // Enum values: 'ACTIVE', 'INACTIVE', 'SUSPENDED'

    @ApiModelProperty(value = "最后登录时间")
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "是否删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty(value = "角色")
    @TableField("role")
    private String role;  // Enum values: 'USER', 'ADMIN'

    @ApiModelProperty(value = "联系电话")
    @TableField("contact_phone")
    private String contactPhone;

    @ApiModelProperty(value = "头像URL")
    @TableField("avatar_url")
    private String avatarUrl;

}

