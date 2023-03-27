package com.example.seckill.mapper;

import com.example.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author SuYaKang
 * @since 2023-03-28
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("SELECT\n" +
            "g.id,\n" +
            "g.goods_detail,\n" +
            "g.goods_img,\n" +
            "g.goods_name,\n" +
            "g.goods_price,\n" +
            "g.goods_stock,\n" +
            "g.goods_title,\n" +
            "sg.seckill_price,\n" +
            "sg.stock_count,\n" +
            "sg.start_date,\n" +
            "sg.end_date\n" +
            "FROM t_goods as g LEFT JOIN t_seckill_goods as sg ON g.id=sg.goods_id;")
    List<Map<String, Object>> findGoodsVO();

    @Select("SELECT\n" +
            "g.id,\n" +
            "g.goods_detail,\n" +
            "g.goods_img,\n" +
            "g.goods_name,\n" +
            "g.goods_price,\n" +
            "g.goods_stock,\n" +
            "g.goods_title,\n" +
            "sg.seckill_price,\n" +
            "sg.stock_count,\n" +
            "sg.start_date,\n" +
            "sg.end_date\n" +
            "FROM t_goods as g LEFT JOIN t_seckill_goods as sg ON g.id=sg.goods_id\n"+
            "where g.id=#{goodsId};")
    Map<String, Object> findGoodsVOById(Long goodsId);
}
