package com.ocean.dao;

import com.ocean.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * 数据访问层
 */
public interface IUserDao {

    // 1. 定义方法：根据用户名查询用户是否存在
    // 参数：用户名：String
    // 返回值类型：User 对象
    @Select("select * from tab_user where username = #{username}")
    User findByUsername(String username);

    // 2. 定义方法：保存用户信息
    /*
     * 参数：User对象
     * 返回值类型：返回影响的行数 int
     */
    @Insert("insert into tab_user values(null,#{username},#{password},#{name},#{birthday},#{sex},#{telephone},#{email})")
    int saveUser(User user);

}
