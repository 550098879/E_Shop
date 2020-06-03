package org.zyx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class OrderDetail implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "detail_id", type = IdType.AUTO)
      private Integer detailId;

      /**
     * 所属订单
     */
      private Integer orderId;

      /**
     * 商品id
     */
      private Integer goodId;

      /**
     * 购买数量
     */
      private Integer num;


}
