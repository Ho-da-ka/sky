package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 销量排名
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        List<Map<String, Integer>> mapList = orderDetailMapper.countByDate(begin, end);
        if (mapList != null && !mapList.isEmpty()) {
            //创建两个集合，用于存放销量排名和销量数量
            List<String> nameList = new ArrayList<>();
            List<String> numberList = new ArrayList<>();
            for (Map<String, Integer> map : mapList) {
                nameList.add(String.valueOf(map.get("name")));
                // 获取营业额并保留整数部分
                double turnover = Double.parseDouble(String.valueOf(map.get("sum")));
                int integerTurnover = (int) Math.floor(turnover);
                numberList.add(String.valueOf(integerTurnover));
            }
            return SalesTop10ReportVO.builder()
                    .nameList(StringUtils.join(nameList, ","))
                    .numberList(StringUtils.join(numberList, ","))
                    .build();
        }
        return null;
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            //日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        //创建两个集合，用于存放订单总数和有效订单总数
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询当天的订单总数
            Integer orderCount = orderMapper.countByMap(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX), null);
            //查询当天的订单总数
            Integer validOrderCount = orderMapper.countByMap(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX), Orders.COMPLETED);
            orderCountList.add(orderCount == null ? 0 : orderCount);
            validOrderCountList.add(validOrderCount == null ? 0 : validOrderCount);
        }
        Integer orderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        double orderCompletionRate;
        try {
            orderCompletionRate = validOrderCount / orderCount;
        } catch (Exception e) {
            orderCompletionRate = 0.0;
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(orderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            //日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        //创建两个集合，用于新增用户数量和总用户数量
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询当天新增用户数量
            Integer newUser = userMapper.countByMap(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
            //查询当天的总用户数量
            Integer totalUser = userMapper.countByMap(null, LocalDateTime.of(date, LocalTime.MAX));
            newUserList.add(newUser == null ? 0 : newUser);
            totalUserList.add(totalUser);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        // 查询时间区间内的所有订单数据
        List<String> dateList = new ArrayList<>();
        List<String> turnoverList = new ArrayList<>();
        List<Map<String, String>> list = orderMapper.getAmountByDate(begin, end);
        // 遍历结果集，将日期和营业额封装到VO中
        list.forEach(map -> {
            dateList.add(String.valueOf(map.get("date")));
            // 获取营业额并保留整数部分
            double turnover = Double.parseDouble(String.valueOf(map.get("sum")));
            int integerTurnover = (int) Math.floor(turnover);
            turnoverList.add(String.valueOf(integerTurnover));
        });
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setTurnoverList(StringUtils.join(turnoverList, ","));
        turnoverReportVO.setDateList(StringUtils.join(dateList, ","));
        return turnoverReportVO;
    }

}
