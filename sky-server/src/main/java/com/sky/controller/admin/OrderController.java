package com.sky.controller.admin;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@ApiOperation("管理端订单管理")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索:{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 订单详情查询
     */
    @GetMapping("/details/{id}")
    @ApiOperation("订单详情查询")
    public Result<OrderVO> details(@PathVariable Long id) {
        log.info("订单详情查询:{}", id);
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }
    /**
     * 接单
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersDTO ordersDTO) {
        log.info("接单:{}", ordersDTO);
        orderService.confirm(ordersDTO);
        return Result.success();
    }
    /**
     * 拒单
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒单:{}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }
    /**
     * 取消订单
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO OrdersCancelDTO) {
        log.info("取消订单:{}", OrdersCancelDTO);
        orderService.adminCancel(OrdersCancelDTO);
        return Result.success();
    }
    /**
     * 派送订单
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id) {
        log.info("派送订单:{}", id);
        orderService.delivery(id);
        return Result.success();
    }
    /**
     * 完成订单
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id) {
        log.info("完成订单:{}", id);
        orderService.complete(id);
        return Result.success();
    }
}
