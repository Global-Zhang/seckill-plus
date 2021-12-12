package org.example.seckillPlus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.seckillPlus.exception.GlobalException;
import org.example.seckillPlus.mapper.OrderMapper;
import org.example.seckillPlus.pojo.Order;
import org.example.seckillPlus.pojo.SeckillGoods;
import org.example.seckillPlus.pojo.SeckillOrder;
import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.service.IGoodsService;
import org.example.seckillPlus.service.IOrderService;
import org.example.seckillPlus.service.ISeckillGoodsService;
import org.example.seckillPlus.service.ISeckillOrderService;
import org.example.seckillPlus.util.JsonUtil;
import org.example.seckillPlus.vo.GoodsVo;
import org.example.seckillPlus.vo.OrderDetailVo;
import org.example.seckillPlus.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    /*
    * 秒杀
    * */
    @Override
    @Transactional
    public Order seckill(User user, GoodsVo goods) {
        //秒杀商品表减库存
        //获取秒杀商品
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goods.getId()));
        //秒杀商品库存减一
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        //更新秒杀商品库存，返回的库存数大于0且有该商品时返回true
        boolean seckillResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count ="+"stock_count-1")
                                                    .eq("goods_id", goods.getId())
                                                    .gt("stock_count", 0));

        // seckillGoodsService.updateById(seckillGoods);
        if (!seckillResult){
            return null;
        }


        //生成订单
        Order order = new Order();
        order.setUserId(user.getMobile());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getMobile());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);

        //缓存成唯一键
        redisTemplate.opsForValue().set("order:"+user.getMobile()+":"+goods.getId(), JsonUtil.object2JsonStr(seckillOrder));

        return order;
    }



    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (null == orderId) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setGoodsVo(goodsVo);
        detail.setOrder(order);
        return detail;
    }
}
