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
 * 退款记录表
 * </p>
 *
 * @author lxp
 * @since 2025-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("refund_records")
@ApiModel(value="RefundRecords对象", description="退款记录表")
public class RefundRecords implements Serializable {

    private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "退款ID(主键)")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        @ApiModelProperty(value = "订单ID")
    @TableField("order_id")
    private Integer orderId;

        @ApiModelProperty(value = "退款单号")
    @TableField("refund_no")
    private String refundNo;

        @ApiModelProperty(value = "退款金额")
    @TableField("refund_amount")
    private BigDecimal refundAmount;

        @ApiModelProperty(value = "退款原因")
    @TableField("refund_reason")
    private String refundReason;

        @ApiModelProperty(value = "退款状态")
    @TableField("refund_status")
    private Integer refundStatus;

        @ApiModelProperty(value = "完成时间")
    @TableField("complete_time")
    private LocalDateTime completeTime;

        @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

        @ApiModelProperty(value = "操作ID")
    @TableField("operate_id")
    private Integer operateId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }
}
