package com.ocean.dao;

import com.ocean.pojo.Seller;
import org.apache.ibatis.annotations.Select;

/**
 * 商家数据访问层
 */
public interface ISellerDao {
    /**
     * 根据商家id查询商家信息
     */
    @Select("select * from tab_seller where sid = #{sid}")
    Seller findBySid(int sid);
}
