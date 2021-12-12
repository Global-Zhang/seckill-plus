package org.example.seckillPlus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.seckillPlus.exception.GlobalException;
import org.example.seckillPlus.mapper.UserMapper;
import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.service.IUserService;
import org.example.seckillPlus.util.*;
import org.example.seckillPlus.vo.LoginVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.example.seckillPlus.vo.RespBean;
import org.example.seckillPlus.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean login(HttpServletRequest request, HttpServletResponse response,@Valid LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        System.out.println(user.getUsername()+user.getMobile());
        if (null==user){
            throw new GlobalException(RespBeanEnum.LOGINVO_ERROR);
        }
        //校验密码
        if
        (!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGINVO_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket, JsonUtil.object2JsonStr(user));
        request.getSession().setAttribute(ticket,user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    /*UserArgumentResolver*/
    @Override
    public User getByUserTicket(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        String userJson = (String) redisTemplate.opsForValue().get("user:" + userTicket);
        User user = JsonUtil.jsonStr2Object(userJson, User.class);
        if (null != user) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    //对象缓存--看过程即可--通过jmeter测试即可
    @Override
    public RespBean updatePassword(String userTicket, Long id, String password) {

        User user = userMapper.selectById(id);
        if (null == user){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }

        user.setPassword(MD5Util.inputPassToDbPass(password,user.getSalt()));
        int result = userMapper.updateById(user);

        if (1 == result){
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }

        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}