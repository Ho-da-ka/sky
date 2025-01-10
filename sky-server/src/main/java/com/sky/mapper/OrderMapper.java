package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 保存订单数据
     *
     * @param orders
     */
    void submit(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 条件查询订单
     *
     * @param orders
     * @return
     */
    List<Orders> list(Orders orders);
    /**
     * 分页查询订单
     *
     * @param orders
     * @return
     */
    Page<Orders> page(Orders orders);
}
