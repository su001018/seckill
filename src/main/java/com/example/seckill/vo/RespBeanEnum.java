package com.example.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {

    SUCCESS("200","成功"),
    ERROR("500","服务器内部错误"),
    //登录
    LOGIN_ERROR("550","登录名或密码不正确"),
    MOBILE_ERROR("551","手机号码不正确"),
    //秒杀
    EMPTY_STOCK("560","库存不足"),
    REPEAT_ERROR("561","重复秒杀");
    private final String CODE;
    private final String MESSAGE;

}
