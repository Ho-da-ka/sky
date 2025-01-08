package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    void insert(ShoppingCart shoppingCartDTO);

    /**
     * 动态查询购物车
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新购物车
     *
     * @param cart
     */
    void updateNumberById(ShoppingCart cart);

    /**
     * 删除购物车
     *
     * @param shoppingCart
     */
    void delete(ShoppingCart shoppingCart);
}
