package com.sky.mapper;


import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单明细数据
     *
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单id查询订单明细
     *
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long id);

    /**
     * 统计指定日期内销量排名前10
     *
     * @param begin
     * @param end
     * @return
     */
    @MapKey("name")
    List<Map<String, Integer>> countByDate(LocalDate begin, LocalDate end);
}
