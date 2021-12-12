package org.example.seckillPlus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.seckillPlus.pojo.User;
import org.example.seckillPlus.vo.LoginVo;
import org.example.seckillPlus.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserService extends IService<User> {
    /**
     * 登录
     * @param loginVo
     * @return
     */
    RespBean login(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo);

    User getByUserTicket(String userTicket, HttpServletRequest request, HttpServletResponse response);

    /**
     * 更新密码
     * @param userTicket
     * @param id
     * @param password
     * @return
     */
    RespBean updatePassword(String userTicket,Long id,String password);
}
