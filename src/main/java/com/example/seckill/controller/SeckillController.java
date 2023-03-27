package com.example.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.pojo.Order;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.rabbitmq.RabbitMQMessage;
import com.example.seckill.rabbitmq.RabbitMQSender;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IOrderService;
import com.example.seckill.service.ISeckillOrderService;
import com.example.seckill.utils.JsonUtil;
import com.example.seckill.utils.UUIDUtil;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private ISeckillOrderService iSeckillOrderService;
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private DefaultRedisScript script;

    /*
    第一次
    5000 线程 2 循环
    windows：1627qps
    linux：187qps
    redis预减库存
    windows：3339qps
    */
    @RequestMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId) {
        if (user == null) return RespBean.error(RespBeanEnum.LOGIN_ERROR);

        Map<String, Object> goods = iGoodsService.findGoodsVOById(goodsId);
        int stock = (int) goods.get("goodsStock");
        if (stock <= 0) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMESSAGE());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //是否重复下单
        ValueOperations valueOperations = redisTemplate.opsForValue();
        SeckillOrder seckillOrder= (SeckillOrder) valueOperations.get("user:"+user.getId()+":goods:"+goodsId);
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMESSAGE());
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        String uid= UUIDUtil.uuid();
        String lock="stockLock";
        boolean hasLocked=valueOperations.setIfAbsent(lock,uid,60, TimeUnit.SECONDS);
        if(hasLocked){
            try{
                int old=(int)valueOperations.get("goodsStock:"+goodsId);
                if(old>=1){
                    valueOperations.set("goodsStock:"+goodsId,old-1);
                    RabbitMQMessage message=new RabbitMQMessage(user,goodsId);
                    rabbitMQSender.send(JsonUtil.object2JsonStr(message));
                }
            }finally {
                redisTemplate.execute(script, Collections.singletonList(lock),uid);
            }
        }

//        Order order = iOrderService.seckill(user, goods);
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goods);
        return RespBean.success();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        for (Map<String, Object> goods : iGoodsService.findGoodsVO()) {
            valueOperations.set("goodsStock:"+goods.get("id"),goods.get("stockCount"));
        }

    }
}
