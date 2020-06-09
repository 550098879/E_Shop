
package org.zyx.VO;

import lombok.Data;
import org.zyx.entity.GoodsType;

import java.util.List;

@Data
public class AllTypeVO {
    private GoodsType superType;
    private List<GoodsType>  childTypeList;
}
