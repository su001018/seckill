package com.example.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private String code;
    private String message;
    private Object object;
    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCODE(), RespBeanEnum.SUCCESS.getMESSAGE(),null);
    }
    public static RespBean success(Object object){
        return new RespBean(RespBeanEnum.SUCCESS.getCODE(), RespBeanEnum.SUCCESS.getMESSAGE(),object);
    }
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCODE(), respBeanEnum.getMESSAGE(),null);
    }
    public static RespBean error(RespBeanEnum respBeanEnum,Object object){
        return new RespBean(respBeanEnum.getCODE(), respBeanEnum.getMESSAGE(),object);
    }
}
