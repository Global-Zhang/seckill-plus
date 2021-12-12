package org.example.seckillPlus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.seckillPlus.pojo.Goods;
import org.example.seckillPlus.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhoubin
 * @since 1.0.0
 */
public interface IGoodsService extends IService<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
