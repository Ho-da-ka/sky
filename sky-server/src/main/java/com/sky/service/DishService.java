package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {
    /**
     * 新增菜品
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     *
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    void delete(Long[] ids);
    /**
     * 根据id查询菜品和对应的口味信息
     *
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);
    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    void update(DishDTO dishDTO);
    /**
     * 菜品起售停售
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
