package org.example.seckillPlus.controller;

import org.example.seckillPlus.rabbitmq.MQSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class DemoController {

    @Autowired
    private MQSender mqSender;
    /**
     * 测试页面跳转
     *
     * @return
     */
    @RequestMapping("/demo/hello")
    public String hello(Model model) {
        model.addAttribute("name", "xxxx");
        return "hello";
    }

    /*@RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("hello");
    }*/

    /*@RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq() {
        mqSender.send("Hello");
    }*/
    /*

    //direct
    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void mq0001() {
        mqSender.send01("Hello---1");
    }
    //direct
    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void mq0002() {
        mqSender.send02("Hello---2");
    }
    */
/*
    //topic
    @RequestMapping("/mq/topic01")
    @ResponseBody
    public void mq01() {
        mqSender.send001("Hello,Red");
    }
    //topic
    @RequestMapping("/mq/topic02")
    @ResponseBody
    public void mq02() {
        mqSender.send002("Hello,Green");
    }*/
    /*@RequestMapping("/mq/header01")
    @ResponseBody
    public void mq001() {
        mqSender.send0001("Hello,header01");
    }
    @RequestMapping("/mq/header02")
    @ResponseBody
    public void mq002() {
        mqSender.send0002("Hello,header02");
    }*/

}



