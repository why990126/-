package com.ocean.service.impl;

import com.ocean.dao.IFavoriteDao;
import com.ocean.dao.IRouteDao;
import com.ocean.pojo.Favorite;
import com.ocean.pojo.PageBean;
import com.ocean.service.IFavoriteService;
import com.ocean.utils.SqlSessionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 收藏的业务层实现
 */
public class FavoriteServiceImpl implements IFavoriteService {

    // 获得数据访问层代理对象
    private IFavoriteDao favoriteDao = SqlSessionUtils.getMapper(IFavoriteDao.class);
    private IRouteDao routeDao = SqlSessionUtils.getMapper(IRouteDao.class);


    /**
     * 分页查询用户的收藏旅游路线信息
     * 参数：uid和curPage
     * 返回值类型：PageBean对象
     */
    @Override
    public PageBean findPageBeanFavorite(int uid, int curPage) {
        // 1. 查收收藏记录数
        int count = favoriteDao.count(uid);
        // 2. 定义每页显示的记录数
        int pageSize = 4;
        // 3. 计算起始行号
        int start = (curPage - 1) * pageSize;
        // 4. 分页查询收藏信息
        List<Favorite> dataList = favoriteDao.findFavoriteRoute(uid, start, pageSize);
        // 5. 封装PageBean对象
        PageBean pageBean = PageBean.getPageBean(dataList, count, curPage, pageSize);
        // 6. 返回PageBean对象
        return pageBean;
    }

    /**
     * 实现添加收藏路线功能
     * 参数：uid和rid
     * 返回值：返回最新的收藏数量
     */
    @Override
    public int addFavorite(int uid, int rid) {
        // 1. 获得当前系统时间
        Date date = new Date();
        // 2. 将日期对象转换为日期字符串
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        // 3. 调用数据访问层方法：添加收藏信息
        int row = favoriteDao.addFavorite(uid, rid, dateStr);
        if (row == 1) {
            // 4. 更新路线的收藏数量
            routeDao.updateCount(rid);
        }
        // 5. 查询路线的最新收藏数量
        int count = routeDao.findCount(rid);
        // 6. 返回最新收藏数量
        return count;
    }
    @Override
    public int deleteFavorite(int rid) {

        int row = favoriteDao.deleteFavorite(rid);
        if (row == 1) {
            // 4. 更新路线的收藏数量
            routeDao.deleupdateCount(rid);
        }
        // 5. 查询路线的最新收藏数量
        int count = routeDao.findCount(rid);
        // 6. 返回最新收藏数量
        return count;
    }

    /**
     * 根据用户id和旅游路线id判断用户是否收藏了路线
     * @param uid
     * @param rid
     * @returno
     */
    @Override
    public boolean isFavorite(int uid, int rid) {
        return favoriteDao.isFavorite(rid, uid) != null;
    }
}
