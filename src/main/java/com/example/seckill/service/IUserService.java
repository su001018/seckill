package com.example.seckill.service;

import com.example.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.seckill.vo.RespBean;
import com.example.seckill.vo.VOBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SuYaKang
 * @since 2023-03-27
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(VOBean voBean, HttpServletRequest request, HttpServletResponse response);

    Object getUserFromCookie(HttpServletRequest request, HttpServletResponse response, String ticket);
}
