package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param dishFlavors
     */
    void insert(List<DishFlavor> dishFlavors);
    /**
     * 根据菜品id删除口味数据
     * @param DishIds
     */
    void deleteByDishId(Long[] DishIds);
    /**
     * 根据菜品id查询口味数据
     * @param id
     * @return
     */
    List<DishFlavor> getByDishId(Long id);
}
