package org.example.seckillPlus.controller;

import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.service.IGoodsService;
import org.example.seckillPlus.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 商品
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    //静态页面的mvc
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toDetail/{goodsId}" ,produces = "text/html;charset=utf-8")
    //2.0-加入servlet方便thymeleaf渲染，将页面跳转变为返回对象，produce的就是对象
    //public String toDetail(Model model,User user,@PathVariable("goodsId") Long goodsId)
    @ResponseBody
    public String toDetail(Model model,User user,@PathVariable("goodsId") Long goodsId,HttpServletRequest request,HttpServletResponse response){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:"+goodsId);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",user);
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);
        Date endDate = goods.getEndDate();
        Date startDate = goods.getStartDate();
        Date nowDate = new Date();
        //秒杀状态
        int seckillStatus = 0;
        //剩余开始时间
        int remainSeconds = 0;
        //秒杀未开始
        if (nowDate.before(startDate)){
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime())/1000);
        }
        //秒杀已结束
        else if (nowDate.after(endDate)){
            seckillStatus = 2;
            remainSeconds = -1;
        }
        //秒杀中
        else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secKillStatus",seckillStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        //手动渲染，并存入redis，返回页面
        WebContext context = new WebContext(request,response, request.getServletContext(),request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail",context);

        //存入redis
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail"+goodsId,html,60,TimeUnit.SECONDS);
        }

        return html;
        //return "goodsDetail";
    }

    @RequestMapping(value = "toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    //2.0-添加servlet为thymeleaf模板赋值，将页面跳转变为返回对象，produce的就是对象
    //public String toLogin(Model model,User user){
    public String toLogin(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        //3.0-获取redis模板
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String)valueOperations.get("goodsList");
       //3.0- 从redis获取值，为空赋值，不为空调用返回页面
        if (!StringUtils.isEmpty(html)){
            return html;
        }
       model.addAttribute("user",user);
       model.addAttribute("goodsList",goodsService.findGoodsVo());

        //3.0-手动渲染thymeleaf模板
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);

        //3.0-存入缓存
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }

        return html;

       //2.0-不用页面跳转了，改用页面缓存
       //return "goodsList";
    }

    /**
    @Autowired
    private IUserService userService;

    @RequestMapping("/toList")
    //public String toLogin(HttpSession session, Model model, @CookieValue("userTicket") String ticket) {
    public String toLogin(HttpServletRequest request, HttpServletResponse response,Model model,@CookieValue("userTicket")String ticket){
        if (StringUtils.isEmpty(ticket)) {
            return "login";
        }
        //User user = (User) session.getAttribute(ticket);
        User user = userService.getByUserTicket(ticket,request,response);
        if (null == user) {
            return "login";
        }
        model.addAttribute("user", user);
        return "goodsList";
    }
    */
}
