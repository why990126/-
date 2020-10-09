package com.ocean.dao;

import com.ocean.pojo.Favorite;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 收藏表对应数据访问层
 */
public interface IFavoriteDao {
    // 根据用户id查询收藏总记录数
    @Select("select count(*) from tab_favorite where uid = #{uid}")
    int count(int uid);

    /** 根据用户id分页查询收藏信息
     *  参数：uid，start，pageSize
     *  返回值：List<Favorite>
     */
    @Select("select * from tab_favorite where uid = #{uid} order by date desc limit #{start},#{pageSize}")
    @Results({
           @Result(column = "date",property = "date"),
            /* 封装路线信息：一个收藏记录就对应一条路线：一对一关系*/
           @Result(column = "rid",
                   property = "route",
                   one = @One(
                           select = "com.ocean.dao.IRouteDao.findRouteById"
                   )
           )
    })
    List<Favorite> findFavoriteRoute(@Param("uid") int uid,
                                         @Param("start") int start,
                                         @Param("pageSize") int pageSize);

    // 添加收藏：收藏路线
    @Insert("insert into tab_favorite value(#{rid},#{date},#{uid});")
    int addFavorite(@Param("uid") int uid,
                    @Param("rid") int rid,
                    @Param("date") String date);
    // 删除收藏
    @Insert("delete from tab_favorite where rid=#{rid};")
    int deleteFavorite(@Param("rid") int rid);

    // 判断用户是否收藏了旅游路线
    @Select("select * from tab_favorite where uid = #{uid} and rid = #{rid}")
    Favorite isFavorite(@Param("rid") int rid, @Param("uid") int uid);
}


