package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    /**
     * 保存订单数据
     * @param orders
     */
    void submit(Orders orders);
}
