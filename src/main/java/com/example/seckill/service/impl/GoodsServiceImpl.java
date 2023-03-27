package com.example.seckill.service.impl;

import com.example.seckill.pojo.Goods;
import com.example.seckill.mapper.GoodsMapper;
import com.example.seckill.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public List<Map<String, Object>> findGoodsVO() {
        return goodsMapper.findGoodsVO();
    }

    @Override
    public Map<String, Object> findGoodsVOById(Long goodsId) {
        return goodsMapper.findGoodsVOById(goodsId);
    }
}
