package org.example.seckillPlus.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiverPrac {

   /*
   //简单模式
   @RabbitListener(queues = "queue")
    public void receive(Object msg){
        log.info("接收消息" + msg);
    }*/

    /*
    //fanout
    @RabbitListener(queues = "queue_fanout01")
    public void receive01(Object msg) {
        log.info("fanout01--QUEUE01接受消息：" + msg);
    }
    @RabbitListener(queues = "queue_fanout02")
    public void receive02(Object msg) {
        log.info("fanout02--QUEUE02接受消息：" + msg);
    }
    */
    /*
    //direct
    @RabbitListener(queues = "queue_direct01")
    public void receive001(Object msg) {
        log.info("queue_direct01--QUEUE01接受消息：" + msg);
    }
    //direct
    @RabbitListener(queues = "queue_direct02")
    public void receive002(Object msg) {
        log.info("queue_direct02--QUEUE02接受消息：" + msg);
    }
*/

    /*

    //topic
    @RabbitListener(queues = "queue_topic01")
    public void receive001(Object msg) {
        log.info("queue_topic01--QUEUE01接受消息：" + msg);
    }
    //topic
    @RabbitListener(queues = "queue_topic02")
    public void receive002(Object msg) {
        log.info("queue_topic02--QUEUE02接受消息：" + msg);
    }
    */

   /* @RabbitListener(queues = "queue_header01")
    public void receive0001(Message message) {
        log.info("queue_header01--QUEUE01接受Message对象：" + message);
        log.info("queue_header01--QUEUE01接受消息：" + new String(message.getBody()));
    }
    @RabbitListener(queues = "queue_header02")
    public void receive0002(Message message) {
        log.info("queue_header02--QUEUE02接受Message对象：" + message);
        log.info("queue_header02--QUEUE02接受消息：" + new String(message.getBody()));
    }*/

}
