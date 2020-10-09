package com.ocean.service;

/**
 * 分类业务层接口
 */
public interface ICategoryService {
    /**
     * 查询所有旅游分类数据
     * 参数：无
     * 返回值：json字符串
     * */
    public String findAllCategories();
}
