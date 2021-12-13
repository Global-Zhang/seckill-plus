package org.example.seckillPlus.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfigPrac {
    //简单模式
    /*@Bean
    public Queue queue(){
        return new Queue("Queue",true);
    }*/
    /*
    //fanout
    private static final String QUEUE01 = "queue_fanout01";
    private static final String QUEUE02 = "queue_fanout02";
    private static final String EXCHANGE = "fanoutExchange";
    //fanout
    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    //fanout
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    //fanout
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE);
    }
    //fanout
    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(fanoutExchange());
    }
    //fanout
    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(fanoutExchange());
    }
    */

    /*

    //direct
    private static final String QUEUE01 = "queue_direct01";
    private static final String QUEUE02 = "queue_direct02";
    private static final String EXCHANGE = "directExchange";
    private static final String ROUTINGKEY01 = "queue.red";
    private static final String ROUTINGKEY02 = "queue.green";
    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    //direct
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE);
    }
    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
    }

    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
    }
    */
    /*

    //topic
    private static final String QUEUE01 = "queue_topic01";
    private static final String QUEUE02 = "queue_topic02";
    private static final String EXCHANGE = "topicExchange";
    private static final String ROUTINGKEY01 = "#.queue.#";
    private static final String ROUTINGKEY02 = "*.queue.#";
    @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }
    //topic
    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(queue01()).to(topicExchange()).with(ROUTINGKEY01);
    }

    @Bean
    public Binding binding02(){
        return BindingBuilder.bind(queue02()).to(topicExchange()).with(ROUTINGKEY02);
    }
    */

    //header
   /* private static final String QUEUE01 = "queue_header01";
    private static final String QUEUE02 = "queue_header02";
    private static final String EXCHANGE = "headersExchange";
     @Bean
    public Queue queue01(){
        return new Queue(QUEUE01);
    }
    @Bean
    public Queue queue02(){
        return new Queue(QUEUE02);
    }
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(EXCHANGE);
    }
    @Bean
     public Binding binding01(){
         Map<String,Object> map = new HashMap<>();
         map.put("color","red");
         map.put("speed","low");
         return
            BindingBuilder.bind(queue01()).to(headersExchange()).whereAny(map).match();
     }
     @Bean
     public Binding binding02(){
         Map<String,Object> map = new HashMap<>();
         map.put("color","red");
         map.put("speed","fast");
         return
            BindingBuilder.bind(queue02()).to(headersExchange()).whereAll(map).match();
     }*/
}
