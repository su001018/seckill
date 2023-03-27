package com.example.seckill.rabbitmq;

import com.example.seckill.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message){
        log.info("发送消息："+message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,"seckill.queue",message);
    }


}
