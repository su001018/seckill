package com.example.seckill.rabbitmq;

import com.example.seckill.config.RabbitMQConfig;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RabbitMQReceiver {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IOrderService orderService;

    @RabbitListener(queues = {RabbitMQConfig.SECKILL_QUEUE})
    public void receive(String message){
        log.info("收到消息"+message);
        RabbitMQMessage map= JsonUtil.jsonStr2Object(message, RabbitMQMessage.class);
        Long goodsId=map.getGoodsId();
        User user=map.getUser();

        Map<String, Object> goodsVOById = goodsService.findGoodsVOById(goodsId);
        if((int)goodsVOById.get("goodsStock")<1){
            return;
        }
        //是否重复下单
        ValueOperations valueOperations = redisTemplate.opsForValue();
        SeckillOrder seckillOrder= (SeckillOrder) valueOperations.get("user:"+user.getId()+":goods:"+goodsId);
        if (seckillOrder != null) {
            return;
        }
        orderService.seckill(user,goodsVOById);



    }


}
