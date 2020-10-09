package com.ocean.dao;

import com.ocean.pojo.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 旅游分类数据DAO
 */
public interface ICategoryDao {
   /**
    * 根据分类id查询分类信息
    * @return
    */
   @Select("select * from tab_category where cid = #{cid}")
   Category findByCid(int cid);

   /* 定义方法：查询所有旅游分类信息
    参数：无参数
    返回值类型：List<Category>
    执行的sql语句：select * from tab_category order by cid;
    */
   @Select("select * from tab_category order by cid")
   public List<Category> findAllCategories();
}
