package com.ocean.dao;

import com.ocean.pojo.Category;
import com.ocean.pojo.Route;
import com.ocean.pojo.Seller;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 旅游路线数据访问层
 */
public interface IRouteDao {

   /* //  查询收藏排行榜总记录数
    @Select("select count(*) from tab_route where rflag = 1")
    int countForRank();
    // 分页查询收藏排行榜数据
    @Select("select * from tab_route where rflag = 1 order by count desc limit #{start},#{pageSize}")
    List<Route> findRoutesForRank(@Param("start") int start,
                                  @Param("pageSize") int pageSize);*/

    // 使用动态sql，sql语句推荐使用xml配置
    // 查询收藏排行榜总记录数


    int countForRank(@Param("map") Map<String,String> map);


    // 分页查询收藏排行榜数据
    List<Route> findRoutesForRank(@Param("start") int start,
                                  @Param("pageSize") int pageSize,
                                  @Param("map") Map<String,String> map);

    /**
     * 根据路线id查询路线信息
     */
    @Select("select * from tab_route where rid = #{rid}")
    Route findRouteById(int rid);

    /**
     * 根据rid查询收藏数量
     */
    @Select("select count from tab_route where rid = #{rid}")
    int findCount(int rid);

    /**
     * 更新路线的收藏数量
     */
    @Update("update tab_route set count = count + 1 where rid = #{rid}")
    int updateCount(int rid);

    /**
     * 更新路线的收藏数量
     */
    @Update("update tab_route set count = count - 1 where rid = #{rid}")
    int deleupdateCount(int rid);


    /**
     * 根据路线id查询路线信息
     */
    @Select("select * from tab_route where rid = #{rid}")
    @Results({
                    // @Result作用：用来设置实体类属性与数据表字段的对应关系
                    @Result(property = "rid",column = "rid",id = true),
                    @Result(property = "cid",column = "cid"),
                    @Result(property = "sid",column = "sid"),
                    // 封装分类数据，一条旅游路线属于一个分类：一对一关系
                    // select就是关联查询的sql，格式：namespace+"."+id
                    @Result(property = "category",
                            column = "cid",
                            javaType = Category.class,
                            one=@One(
                            select = "com.ocean.dao.ICategoryDao.findByCid"
                    )),
                    // 封装商家数据：一条旅游路线对应一个商家：一对一关系
                    @Result(
                            property = "seller",
                            column = "sid",
                            javaType = Seller.class,
                            one = @One(
                                    select = "com.ocean.dao.ISellerDao.findBySid"
                            )
                    ),
                    // 封装旅游图片信息：一条旅游路线对应多张图片：一对多关系
                    @Result(
                            property = "routeImgList",
                            javaType = List.class,
                            column = "rid",
                            many = @Many(
                                    select = "com.ocean.dao.IRouteImageDao.findByRid"
                            )
                    )
        }
    )
    Route findByRid(int rid);

    // 方法1：根据分类id查询旅游路线总记录数
    /**
     * 根据分类id查询旅游路线总记录数
     * @param cid
     * @param rname 搜索条件：旅游路线名字 : %双飞%
     * @return
     */
    @Select("select count(*) from tab_route where cid = #{cid} and rname like concat('%',#{rname},'%')")
    int count(@Param("cid") int cid,
              @Param("rname") String rname);

    // 方式2：根据分类id分页查询旅游路线数据
    @Select("select * from tab_route where cid = #{cid} and rname like concat('%',#{rname},'%') limit #{start}, #{pageSize}")
    List<Route> findRoutesByCid(@Param("cid")   int cid,
                                @Param("start") int start,
                                @Param("pageSize") int pageSize,
                            @Param("rname") String rname);

    // 人气旅游：是每个旅游线路收藏数量的降序获取前4条数据进行显示
    @Select("select * from tab_route order by count desc limit 0,4")
    List<Route> findRoutesPopularity();

    // 最新旅游：是每个旅游线路上架时间的降序获取前4条数据进行显示
    @Select("select * from tab_route order by rdate desc limit 0,4")
    List<Route> findRoutesNewest();

    // 主题旅游：是每个旅游线路过滤为主题旅游的获取前4条数据进行显示
    @Select("select * from tab_route where isThemeTour = 1 order by rdate desc  limit  0,4")
    List<Route> findRoutesTheme();
}
