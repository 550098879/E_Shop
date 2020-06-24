package org.zyx.VO;

import lombok.Data;
import org.zyx.entity.Goods;
import org.zyx.entity.GoodsType;

@Data
public class GoodsVO {

    private Goods goods;
    private String typeName;
    private String superName;
    private String cover;//封面
    private int num;//商品购买的数量
    private String reqCover;

}
