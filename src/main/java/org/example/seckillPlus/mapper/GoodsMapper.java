package org.example.seckillPlus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.seckillPlus.pojo.Goods;
import org.example.seckillPlus.vo.GoodsVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 根据商品id获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
