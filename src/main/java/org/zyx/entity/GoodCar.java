package org.zyx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class GoodCar implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 购物车id
     */
        @TableId(value = "car_id", type = IdType.AUTO)
      private Integer carId;

    private Integer buyerId;

      /**
     * 商品id
     */
      private Integer goodsId;

      /**
     * 购买数量
     */
      private Integer num;

    private LocalDateTime addTime;


}
