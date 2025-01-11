package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> page(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态查询订单
     *
     * @param status
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{localDateTime}")
    List<Orders> getByStateAndTime(int status, LocalDateTime localDateTime);

    /**
     * 根据日期统计营业额
     *
     * @param begin
     * @param end
     * @return
     */
    @MapKey("date")
    List<Map<String, String>> getAmountByDate(LocalDate begin, LocalDate end);

    /**
     * 根据状态统计订单数量
     * @param end
     * @param begin
     * @param status
     * @return
     */
    Integer countByMap(LocalDateTime begin, LocalDateTime end, Integer status);
}
