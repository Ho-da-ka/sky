package com.sky.service.impl;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
