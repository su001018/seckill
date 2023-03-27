package com.example.seckill.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.seckill.pojo.User;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.IUserService;
import com.example.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;


    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    /*
    第一次
    5000 线程 2 循环
    windows：2388qps
    linux：478qps
    添加页面缓存之后：
    windows： 5711qps
     */
    //添加页面缓存
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html=(String)valueOperations.get("goodsList");
        if(!StringUtils.isBlank(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",iGoodsService.findGoodsVO());
        WebContext context=new WebContext(request, response,
                request.getServletContext(), request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if(!StringUtils.isBlank(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;
//        return "goodsList";

    }

    @RequestMapping(value = "/toDetail/{GoodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model, User user, @PathVariable Long GoodsId,HttpServletRequest request,
                           HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html= (String) valueOperations.get("goodsDetail:"+GoodsId);
        if (!StringUtils.isBlank(html)) {
            return html;
        }
        model.addAttribute("user",user);
        Map<String,Object> goods=iGoodsService.findGoodsVOById(GoodsId);
        LocalDateTime sDate=(LocalDateTime) goods.get("startDate");
        LocalDateTime eDate=(LocalDateTime) goods.get("endDate");
        LocalDateTime nDate=LocalDateTime.now();
        int secKillStatus=0;
        int remainSeconds= (int) (sDate.toEpochSecond(ZoneOffset.UTC)-nDate.toEpochSecond(ZoneOffset.UTC));
        if(nDate.isBefore(eDate)&&nDate.isAfter(sDate)){
            secKillStatus=1;
            remainSeconds=0;
        }else if(nDate.isAfter(eDate)){
            secKillStatus=2;
            remainSeconds=-1;
        }
        model.addAttribute("secKillStatus",secKillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("goods",goods);
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", new WebContext(
                request, response, request.getServletContext(), request.getLocale(), model.asMap()
        ));
        if(!StringUtils.isBlank(html)){
            valueOperations.set("goodsDetail:"+GoodsId,html,60,TimeUnit.SECONDS);
        }
        return html;
//        return "goodsDetail";
    }

    //静态页面+返回数据
    @RequestMapping(value = "/detail/{GoodsId}")
    @ResponseBody
    public RespBean detail(User user, @PathVariable Long GoodsId){
        Map<String,Object>model=new HashMap<>();
        model.put("user",user);
        Map<String,Object> goods=iGoodsService.findGoodsVOById(GoodsId);
        LocalDateTime sDate=(LocalDateTime) goods.get("startDate");
        LocalDateTime eDate=(LocalDateTime) goods.get("endDate");
        LocalDateTime nDate=LocalDateTime.now();
        int secKillStatus=0;
        int remainSeconds= (int) (sDate.toEpochSecond(ZoneOffset.UTC)-nDate.toEpochSecond(ZoneOffset.UTC));
        if(nDate.isBefore(eDate)&&nDate.isAfter(sDate)){
            secKillStatus=1;
            remainSeconds=0;
        }else if(nDate.isAfter(eDate)){
            secKillStatus=2;
            remainSeconds=-1;
        }
        model.put("secKillStatus",secKillStatus);
        model.put("remainSeconds",remainSeconds);
        model.put("goodsVo",goods);
        return RespBean.success(model);
    }
}
