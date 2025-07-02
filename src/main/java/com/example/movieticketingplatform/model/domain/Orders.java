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
 * 订单表
 * </p>
 *
 * @author lxp
 * @since 2025-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
@ApiModel(value="Orders对象", description="订单表")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "订单ID(主键)")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        @ApiModelProperty(value = "订单编号")
    @TableField("order_no")
    private String orderNo;

        @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

        @ApiModelProperty(value = "场次ID")
    @TableField("session_id")
    private Integer sessionId;

        @ApiModelProperty(value = "订单总金额")
    @TableField("total_amount")
    private BigDecimal totalAmount;

        @ApiModelProperty(value = "联系电话")
    @TableField("contact_phone")
    private String contactPhone;

        @ApiModelProperty(value = "订单状态")
    @TableField("order_status")
    private Byte orderStatus;

        @ApiModelProperty(value = "支付方式")
    @TableField("payment_method")
    private String paymentMethod;

        @ApiModelProperty(value = "支付时间")
    @TableField("payment_time")
    private LocalDateTime paymentTime;

        @ApiModelProperty(value = "支付交易号")
    @TableField("payment_transaction_id")
    private String paymentTransactionId;

        @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

        @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

        @ApiModelProperty(value = "订单座位")
    @TableField("code")
    private String code;


}
