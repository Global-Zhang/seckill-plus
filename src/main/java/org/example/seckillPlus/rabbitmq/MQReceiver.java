package org.example.seckillPlus.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.seckillPlus.pojo.Order;
import org.example.seckillPlus.pojo.SeckillOrder;
import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.service.IGoodsService;
import org.example.seckillPlus.service.IOrderService;
import org.example.seckillPlus.util.JsonUtil;
import org.example.seckillPlus.vo.GoodsVo;
import org.example.seckillPlus.vo.RespBean;
import org.example.seckillPlus.vo.RespBeanEnum;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {
        log.info("QUEUE接受消息：" + msg);
        SeckillMessage message = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        Long goodsId = message.getGoodsId();
        User user = message.getUser();
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            return ;
        }
        //判断是否重复抢购
        // SeckillOrder seckillOrder = seckillOrderService.getOne(new
        //QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id",goodsId));

        String seckillOrderJson = (String) redisTemplate.opsForValue().get("order:" + user.getMobile() + ":" + goodsId);
        if (!StringUtils.isEmpty(seckillOrderJson)) {
            return ;
        }

         orderService.seckill(user, goods);

    }


}
