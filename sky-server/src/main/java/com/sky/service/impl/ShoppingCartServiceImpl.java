package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    @Transactional
    public void add(ShoppingCartDTO shoppingCartDTO) {
        // 查询当前菜品或套餐是否在购物车中
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        // 如果已经存在，就在原来数量基础上加一
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
            return;
        }
        // 如果不存在，则添加到购物车，数量默认就是一
        if (shoppingCart.getDishId() != null) {
            List<Dish> dishes = dishMapper.getById(new Long[]{shoppingCart.getDishId()});
            Dish dish = dishes.get(0);
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setName(dish.getName());
            shoppingCart.setDishFlavor(shoppingCartDTO.getDishFlavor());
        } else {
            SetmealVO setmealVO = setmealMapper.getById(shoppingCart.getSetmealId());
            shoppingCart.setImage(setmealVO.getImage());
            shoppingCart.setName(setmealVO.getName());
            shoppingCart.setAmount(setmealVO.getPrice());
        }
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCart.setNumber(1);
        shoppingCartMapper.insert(shoppingCart);
    }
}
