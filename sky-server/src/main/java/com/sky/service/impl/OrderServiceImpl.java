package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.AddressBookService;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        // 判断地址是否为空
        AddressBook addressBook = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new ArithmeticException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(ShoppingCart.builder().userId(BaseContext.getCurrentId()).build());
        // 判断购物车是否为空
        if (shoppingCarts.size() == 0 || shoppingCarts == null) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        // 生成订单
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setOrderTime(LocalDateTime.now());
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setConsignee(addressBook.getConsignee());
        ordersMapper.submit(orders);
        // 生成订单详情
        List<OrderDetail> orderDetails = new ArrayList<>();
        shoppingCarts.forEach(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);

        });
        orderDetailMapper.insertBatch(orderDetails);
        // 清空购物车
        shoppingCartMapper.delete(ShoppingCart.builder().userId(BaseContext.getCurrentId()).build());

        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }
}
