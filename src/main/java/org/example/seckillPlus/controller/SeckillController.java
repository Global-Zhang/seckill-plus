package org.example.seckillPlus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.example.seckillPlus.config.AccessLimit;
import org.example.seckillPlus.exception.GlobalException;
import org.example.seckillPlus.pojo.Order;
import org.example.seckillPlus.pojo.SeckillOrder;
import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.rabbitmq.MQReceiver;
import org.example.seckillPlus.rabbitmq.MQSender;
import org.example.seckillPlus.rabbitmq.SeckillMessage;
import org.example.seckillPlus.service.IGoodsService;
import org.example.seckillPlus.service.IOrderService;
import org.example.seckillPlus.service.ISeckillOrderService;
import org.example.seckillPlus.util.JsonUtil;
import org.example.seckillPlus.vo.GoodsVo;
import org.example.seckillPlus.vo.RespBean;
import org.example.seckillPlus.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private DefaultRedisScript script;
    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();
    //????????????  {path}
    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();

        //???????????????????????????????????????????????????????????????redis???????????????????????????????????????????????????
        String seckillOrderJson = (String) valueOperations.get("order:" + user.getMobile() + ":" + goodsId);

        if (null != seckillOrderJson) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        //?????????????????????redis??????
        if (EmptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //????????????
        /*
        if (redis.call('exists', KEYS[1]) == 1) then
           local stock = tonumber(redis.call('get', KEYS[1]));
           if (stock > 0) then
              redis.call('incrby', KEYS[1], -1);
              return stock;
           end;
            return 0;
        end;
        */
        //Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        Long stock = (Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if (stock <= 0) {
            EmptyStockMap.put(goodsId, true);
           // valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // ????????????????????????????????????
        SeckillMessage message = new SeckillMessage(user, goodsId);
        mqSender.sendsecKillMessage(JsonUtil.object2JsonStr(message));

        return RespBean.success(0);
    }

    /*
    * ????????????????????????????????????????????????Redis
    * */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            System.out.println(goodsVo);
            EmptyStockMap.put(goodsVo.getId(), false);
        });
    }

    /**
     * ??????????????????
     *
     * @param user
     * @param goodsId
     * @return orderId:?????????-1??????????????????0????????????
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }


    /**
     * ??????????????????
     * ????????????+?????????????????????????????????????????????????????????????????????????????????
     *??????????????????
     * @param user
     * @param goodsId
     * @return
     */
    //????????????????????????
    //??????????????????????????????UserContext???UserArgumentResolver???AccessInterceptor???WebConfig???AccessLimit
    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha,
                            HttpServletRequest request) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //????????????
        captcha = "0";
        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }
    //??????????????????
    /*??????????????????
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha,
                            HttpServletRequest request) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //?????????????????????5????????????5???
        String uri = request.getRequestURI();
        //????????????
        captcha = "0";
        Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
        if (count==null){
            valueOperations.set(uri + ":" + user.getId(),1,5,TimeUnit.SECONDS);
        }else if (count<5){
            valueOperations.increment(uri + ":" + user.getId());
        }else {
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
        }
        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }
    */
    /**
     * ?????????
     *
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
        if (null==user||goodsId<0){
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        // ????????????????????????????????????
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //?????????????????????????????????redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);

        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text
                (),300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("?????????????????????",e.getMessage());
        }
    }
}


    /*
    //redis???????????????
    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //????????????
        if (goods.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //???????????????????????????????????????????????????????????????redis???????????????????????????????????????????????????
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
    */

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

        //????????????
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
