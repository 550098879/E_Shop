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
 * 
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class Buyer implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "buyer_id", type = IdType.AUTO)
      private Integer buyerId;

    private String buyerName;

      /**
     * 登录账号
     */
      private String account;

    private String psw;

      /**
     * 余额
     */
      private BigDecimal balance;

      /**
     * 支付密码
     */
      private String payPsw;

      /**
     * 头像
     */
      private String face;

    private LocalDateTime createTime;


}
