package org.zyx.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class Goods implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "good_id", type = IdType.AUTO)
      private Integer goodId;

    private String goodsName;

    private Integer typeId;

    private Integer superId;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String remark;


}
