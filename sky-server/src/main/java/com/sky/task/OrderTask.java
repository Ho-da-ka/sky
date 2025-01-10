package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务,定时处理超时订单
 *
 * @author qq:fgj
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 每分钟检查一次订单是否超时未支付，并自动取消这些订单
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void orderTimeOutConfirm() {
        // 记录订单超时处理开始时间
        log.info("订单超时，开始处理:{}", LocalDateTime.now());
        // 获取待支付状态的订单
        int status = Orders.PENDING_PAYMENT;
        // 获取当前时间前15分钟的时间点，用于判断订单是否超时
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);
        // 调用订单Mapper根据状态和时间获取订单列表
        List<Orders> ordersList = orderMapper.getByStateAndTime(status, localDateTime);
        // 如果存在超时订单，则遍历并更新它们的状态为已取消
        if (ordersList != null && ordersList.size() > 0)
            ordersList.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            });
        // 记录订单超时处理完成
        log.info("订单超时，处理完成");
    }

    /**
     * 每天凌晨2点检查并更新所有正在派送中的订单状态为已完成
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void orderDeliveryConfirm() {
        // 记录订单派送处理开始时间
        log.info("订单派送，开始处理:{}", LocalDateTime.now());
        // 获取所有正在派送中的订单
        List<Orders> ordersList = orderMapper.list(Orders.builder().status(Orders.DELIVERY_IN_PROGRESS).build());
        // 如果存在正在派送中的订单，则遍历并更新它们的状态为已完成
        if (ordersList != null && ordersList.size() > 0)
            ordersList.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            });
        // 记录订单派送处理完成
        log.info("订单派送，处理完成");
    }
}
