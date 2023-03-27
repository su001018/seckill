package com.example.seckill.controller;


import com.example.seckill.pojo.User;
import com.example.seckill.rabbitmq.RabbitMQSender;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author SuYaKang
 * @since 2023-03-27
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @RequestMapping("/info")
    @ResponseBody
    public RespBean userInfo(User user){
        return RespBean.success(user);
    }


    @RequestMapping("/mq")
    @ResponseBody
    public void send(){
        rabbitMQSender.send("hello ");
    }
}
