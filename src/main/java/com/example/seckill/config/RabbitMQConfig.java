package com.example.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.Bindings;


@Configuration
public class RabbitMQConfig {
    public static final String SECKILL_QUEUE="seckillQueue";
    public static final String EXCHANGE="topicExchange";
    public static final String ROUTINIG_KEY="seckill.#";

    @Bean
    public Queue queue(){
        return new Queue(SECKILL_QUEUE);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with(ROUTINIG_KEY);
    }
}
