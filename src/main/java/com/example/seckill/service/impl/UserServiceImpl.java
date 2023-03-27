package com.example.seckill.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.seckill.pojo.User;
import com.example.seckill.mapper.UserMapper;
import com.example.seckill.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.seckill.utils.CookieUtil;
import com.example.seckill.utils.MD5Util;
import com.example.seckill.utils.UUIDUtil;
import com.example.seckill.utils.ValidatorUtil;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.RespBeanEnum;
import com.example.seckill.vo.VOBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author SuYaKang
 * @since 2023-03-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(VOBean voBean, HttpServletRequest request, HttpServletResponse response) {
        String mobile=voBean.get("mobile").toString();
        String password=voBean.get("password").toString();
        if(StringUtils.isBlank(mobile)||StringUtils.isBlank(password)){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }
        User user = userMapper.selectById(mobile);
        if(null==user){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        if(!user.getPassword().equals(MD5Util.formPassToDBPass(password,user.getSlat()))){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        //制造cookie
        String ticket= UUIDUtil.uuid();
//        request.getSession().setAttribute(ticket,user);
        redisTemplate.opsForValue().set("user:"+ticket,user);
        CookieUtil.setCookie(request,response,"UserTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public User getUserFromCookie(HttpServletRequest request, HttpServletResponse response, String ticket) {
        User user=null;
        user=(User)redisTemplate.opsForValue().get("user:"+ticket);
        if(user!=null){
            CookieUtil.setCookie(request,response,"UserTicket",ticket);
        }
        return user;
    }
}
