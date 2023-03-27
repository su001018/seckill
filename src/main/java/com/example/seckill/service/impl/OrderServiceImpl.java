package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.seckill.mapper.SeckillGoodsMapper;
import com.example.seckill.pojo.Order;
import com.example.seckill.mapper.OrderMapper;
import com.example.seckill.pojo.SeckillGoods;
import com.example.seckill.pojo.SeckillOrder;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.service.ISeckillGoodsService;
import com.example.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SuYaKang
 * @since 2023-03-28
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService iSeckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService iSeckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    @Transactional
    public Order seckill(User user, Map<String, Object> goods) {
        Long goodsId=(Long)goods.get("id");
        SeckillGoods seckillGoods=iSeckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goodsId));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        //乐观锁解决超卖
//        boolean res= iSeckillGoodsService.update(new UpdateWrapper<SeckillGoods>().eq("id",goodsId).gt("stock_count",0)
//                .eq("stock_count",seckillGoods.getStockCount()+1).set("stock_count",seckillGoods.getStockCount()));
        boolean seckillGoodsResult = iSeckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count = " + "stock_count-1")
                .eq("goods_id", goodsId)
                .gt("stock_count", 0)
        );

        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsId);
        order.setDeliveryAddrId(0L);
        order.setGoodsName((String)goods.get("goodsName"));
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsId);
        iSeckillOrderService.save(seckillOrder);

        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("user:"+user.getId()+":goods:"+goodsId,seckillOrder);
        return order;

    }
}
