package org.example.seckillPlus.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSenderPrac {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
    //简单模式
    public void send(Object msg){
        log.info("发送消息：" + msg);
        rabbitTemplate.convertAndSend("queue",msg);
    }*/

    /*
    //fanout
    public void send(Object msg) {
        log.info("发送消息："+msg);
        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
    }
    */
    /*

    public void send01(Object msg) {
        log.info("发送red消息："+msg);
        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
    }

    public void send02(Object msg) {
        log.info("发送green消息："+msg);
        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);
    }
    */

    /*

    //topic
    public void send001(Object msg) {
        log.info("发送消息(被01队列接受)："+msg);
        rabbitTemplate.convertAndSend("topicExchange","queue.red.message",msg);
     }
     //topic
     public void send002(Object msg) {
        log.info("发送消息(被两个queue接受)："+msg);
        rabbitTemplate.convertAndSend("topicExchange","message.queue.green.abc",msg);
     }
    */


    /*

    //header
    public void send0001(String msg) {
        log.info("发送消息(被两个queue接受)：" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("color", "red");
        properties.setHeader("speed", "fast");
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.convertAndSend("headersExchange", "", message);
    }
    //header
    public void send0002(String msg) {
        log.info("发送消息(被01队列接受)：" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("color", "red");
        properties.setHeader("speed", "normal");
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.convertAndSend("headersExchange", "", message);
    }
    */

}
