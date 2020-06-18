package org.zyx.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.INPUT)
    private Integer orderId;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 收货时间
     */
    private LocalDateTime teceiveTime;

    private String address;

    private String phone;

    private String nicheng;

    /**
     * 所属买家
     */
    private Integer buyerId;

    /**
     * 金额
     */
    private BigDecimal total;

    /**
     * 状态(0:创建 1:发货  2:已收货)
     */
    private Integer status;


}
