package org.zyx.service;

import org.zyx.VO.AllTypeVO;
import org.zyx.entity.GoodsType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
public interface GoodsTypeService extends IService<GoodsType> {

    List<AllTypeVO> getAllType();


}
