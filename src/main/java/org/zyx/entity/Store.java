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
 * 买家充值表
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class Store implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "store_id", type = IdType.AUTO)
      private Integer storeId;

      /**
     * 买家主键
     */
      private Integer buyerId;

      /**
     * 充值时间
     */
      private LocalDateTime storeTime;

      /**
     * 充值金额
     */
      private BigDecimal amount;


}
