package com.example.seckill.service;

import com.example.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author SuYaKang
 * @since 2023-03-28
 */
public interface IGoodsService extends IService<Goods> {

    List<Map<String,Object>> findGoodsVO();

    Map<String,Object> findGoodsVOById(Long goodsId);
}
