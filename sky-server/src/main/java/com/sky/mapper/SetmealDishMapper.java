package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询套餐id
     */
    List<Long> getSetmealIdsByDishId(Long[] dishIds);

    /**
     * 批量插入套餐菜品关系
     */
    void insert(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐菜品关系
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);


    /**
     * 根据套餐id批量删除套餐菜品关系
     */
    void deleteBySetmealIds(Long[] setmealIds);
}
