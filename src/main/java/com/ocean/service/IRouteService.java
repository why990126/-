package com.ocean.service;

import com.ocean.pojo.PageBean;
import com.ocean.pojo.Route;

import java.util.List;
import java.util.Map;

/**
 * 旅游路线业务层接口
 */
public interface IRouteService {

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
    PageBean findPageBeanForRank(int curPage,Map<String,String> map);

    /*
    * 根据rid查询路线详情
        参数：路线id：rid
        返回值：Route对象
    * */
    Route findRouteDetailByRid(int rid);

    /**
     * 用来分页查询旅游路线数据
     *   	参数：分类id，当前页号
     *      返回值类型：PageBean对象
     */
    PageBean findRoutesByiCid(int cid,int curPage,String rname);

    /**
     * 用来查询 人气旅游 最新旅游  主体旅游 信息
     * key：人气旅游 最新旅游 主体旅游
     * value：
     */
    Map<String, List<Route>> findHeimaJx();
}
