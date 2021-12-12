package org.example.seckillPlus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.seckillPlus.pojo.Order;
import org.example.seckillPlus.pojo.SeckillOrder;
import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.service.IGoodsService;
import org.example.seckillPlus.service.IOrderService;
import org.example.seckillPlus.service.ISeckillOrderService;
import org.example.seckillPlus.vo.GoodsVo;
import org.example.seckillPlus.vo.RespBean;
import org.example.seckillPlus.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购，由之前从数据库判断改为从redis中获取键，如果没有键则不是重复强购
        //SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        String seckillOrderJson = (String) redisTemplate.opsForValue().get("order:"+user.getMobile()+":"+goodsId);

        //if (seckillOrder != null) {
        if (null != seckillOrderJson) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = orderService.seckill(user, goods);
        if (null != order){
            return RespBean.success(order);
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
/*@Controller
@RequestMapping("/seckill")
public class SeckillController{
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;

    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user, Long goodsId){
        if (user == null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);

        //判断库存
        if (goods.getStockCount() < 1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMsg());
            return "seckillFail";
        }
        Order order = orderService.seckill(user,goods);

        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }
}*/
