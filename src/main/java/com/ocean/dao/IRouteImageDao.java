package com.ocean.dao;

import com.ocean.pojo.RouteImg;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 旅游路线图片数据访问层
 */
public interface IRouteImageDao {
    /**
     * 根据路线id查询图片信息
     */
    @Select("select * from tab_route_img where rid = #{rid}")
    List<RouteImg> findByRid(int rid);

}
