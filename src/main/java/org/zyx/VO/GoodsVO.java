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

}
