package com.sky.service.impl;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

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
