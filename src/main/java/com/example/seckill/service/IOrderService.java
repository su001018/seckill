package com.example.seckill.service;

import com.example.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.pojo.User;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SuYaKang
 * @since 2023-03-28
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, Map<String, Object> goods);
}
