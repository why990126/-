package com.ocean.service.impl;

import com.ocean.dao.IRouteDao;
import com.ocean.pojo.PageBean;
import com.ocean.pojo.Route;
import com.ocean.service.IRouteService;
import com.ocean.utils.SqlSessionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务层实现类
 */
public class RouteServiceImpl implements IRouteService {

    // 1. 创建数据访问层对象
    private IRouteDao routeDao = SqlSessionUtils.getMapper(IRouteDao.class);

    /**
     * 定义方法分页查询收藏排行榜数据
     * 	 参数：curPage 当前页号
     * 	 返回值类型：PageBean对象
     * 	 实现步骤：
     * 	 	1. 查询总记录数
     * 	 	2. 定义页大小
     * 	 	3. 计算起始行号
     * 	 	4. 分页查询列表数据
     * 	 	5. 封装PageBean对象
     * 	 	6. 返回PageBean对象
     */
    @Override
    public PageBean findPageBeanForRank(int curPage,Map<String,String> map) {
        // 1. 查询总记录数
        int count = routeDao.countForRank(map);
        // 2. 定义页大小
        int pageSize = 8;
        // 3. 计算起始行号
        int start = (curPage - 1) * pageSize;
        // 4. 分页查询列表数据
        List<Route> dataList = routeDao.findRoutesForRank(start, pageSize,map);
        // 5. 封装PageBean对象
        PageBean pageBean = PageBean.getPageBean(dataList, count, curPage, pageSize);
        // 6. 返回PageBean对象
        return pageBean;
    }

    /**
     * 根据rid查询路线详情
     */
    @Override
    public Route findRouteDetailByRid(int rid) {
        return routeDao.findByRid(rid);
    }

    /**
     * 根据分类id分页查询旅游路线
     * @param cid
     * @param curPage
     * @return
     */
    @Override
    public PageBean findRoutesByiCid(int cid, int curPage,String rname) {
        // 2. 查询总记录数
        int count = routeDao.count(cid,rname);
        // 3.1 定义每页显示的记录数
        int pageSize = 3;
        // 3.2 起始行号
        int start = (curPage - 1) * pageSize;
        // 3.3 分页查询旅游路线列表数据
        List<Route> dataList = routeDao.findRoutesByCid(cid, start, pageSize,rname);
        // 4. 封装PageBean对象
        PageBean pageBean = PageBean.getPageBean(dataList, count, curPage, pageSize);
        // 5. 返回PageBean对象
        return pageBean;
    }

    /**
     * 用来查询 人气旅游 最新旅游  主体旅游 信息
     * key：人气旅游 最新旅游 主体旅游
     * value：
     */
    @Override
    public Map<String, List<Route>> findHeimaJx() {
        // 创建Map封装数据
        Map<String,List<Route>> map = new HashMap<>();
        // 获得人气旅游信息
        List<Route> popularityList = routeDao.findRoutesPopularity();
        // 获得最新旅游信息
        List<Route> newestList = routeDao.findRoutesNewest();
        // 获得主体旅游信息
        List<Route> themeList = routeDao.findRoutesTheme();

        // 存储数据到map集合
        map.put("popularityList", popularityList);
        map.put("newestList", newestList);
        map.put("themeList", themeList);

        return map;
    }
}
