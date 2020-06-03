package org.zyx.serviceImpl;

import org.zyx.entity.GoodCar;
import org.zyx.mapper.GoodCarMapper;
import org.zyx.service.GoodCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author 刈剑丶
 * @since 2020-06-03
 */
@Service
public class GoodCarServiceImpl extends ServiceImpl<GoodCarMapper, GoodCar> implements GoodCarService {

}
