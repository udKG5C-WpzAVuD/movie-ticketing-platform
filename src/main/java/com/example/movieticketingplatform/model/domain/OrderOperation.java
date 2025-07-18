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
 * 订单操作记录表
 * </p>
 *
 * @author lxp
 * @since 2025-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_operation")
@ApiModel(value="OrderOperation对象", description="订单操作记录表")
public class OrderOperation implements Serializable {

    private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "操作ID(主键)")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        @ApiModelProperty(value = "订单ID")
    @TableField("order_id")
    private Integer orderId;

        @ApiModelProperty(value = "操作者类型")
    @TableField("operator_type")
    private Byte operatorType;

        @ApiModelProperty(value = "操作者ID")
    @TableField("operator_id")
    private Integer operatorId;

        @ApiModelProperty(value = "操作类型")
    @TableField("operation")
    private String operation;

        @ApiModelProperty(value = "操作描述")
    @TableField("operation_desc")
    private String operationDesc;

        @ApiModelProperty(value = "操作时间")
    @TableField("operation_time")
    private LocalDateTime operationTime;

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

    public Byte getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Byte operatorType) {
        this.operatorType = operatorType;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }
}
