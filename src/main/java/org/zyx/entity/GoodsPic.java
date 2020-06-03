package org.zyx.entity;

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
public class GoodsPic implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "pic_id", type = IdType.AUTO)
      private Integer picId;

    private Integer picType;

    private String picPath;

    private Integer goodsId;


}
