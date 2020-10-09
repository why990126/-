package com.ocean.service;

import com.ocean.pojo.PageBean;

public interface IFavoriteService {
    /**
     * 分页查询用户的收藏旅游路线信息
     * 参数：uid和curPage
     * 返回值类型：PageBean对象
     */
    PageBean findPageBeanFavorite(int uid,int curPage);

    /**
     * 实现添加收藏路线功能
     * 参数：uid和rid
     * 返回值：返回最新的收藏数量
     */
    int addFavorite(int uid,int rid);
    int deleteFavorite(int rid);

    /**
     * 判断用户是否收藏了指定的旅游路线
     * 参数：用户id和路线id
     * 返回值：boolean  true:收藏了， false：没有收藏
     */
    boolean isFavorite(int uid,int rid);
}
