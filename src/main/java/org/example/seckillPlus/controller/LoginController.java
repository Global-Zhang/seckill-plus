package org.example.seckillPlus.controller;

import org.example.seckillPlus.service.IUserService;
import org.example.seckillPlus.vo.LoginVo;
import org.example.seckillPlus.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private IUserService userService;
    /**
     * 跳转登录页
     *
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }
    /**
     * 登录
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(HttpServletRequest request, HttpServletResponse response, @Valid LoginVo loginVo) {
        RespBean login = userService.login(request,response,loginVo);
        System.out.println(login+"===========");
        return login;
    }


}
