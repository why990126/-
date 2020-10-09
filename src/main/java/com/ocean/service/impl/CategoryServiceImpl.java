package com.ocean.service.impl;

import com.ocean.dao.ICategoryDao;
import com.ocean.pojo.Category;
import com.ocean.service.ICategoryService;
import com.ocean.utils.JedisUtil;
import com.ocean.utils.ObjectMapperUtil;
import com.ocean.utils.SqlSessionUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 分类业务层接口实现类
 */
public class CategoryServiceImpl implements ICategoryService {
    private ICategoryDao categoryDao = SqlSessionUtils.getMapper(ICategoryDao.class);
    /**
     * 查收所有分类数据
     * List<Category
     * String
     * */
    @Override
    public String findAllCategories() {
        try{
            // 1. 从Redis数据查询旅游分类数据
            Jedis jedis = JedisUtil.getJedis();
            String jsonStr = jedis.get("categoryList");
            if (jsonStr == null) {
               System.out.println("从MySQL数据库中查询分类数据...");
               // 1.1 没有查询不到，则代表是第1次访问，则执行第2步
               // 2. 从MySql数据库中查询旅游分类数据
               // 2.1 获得接口代理对象
               // 2.2 调用代理对象方法查询数据
               List<Category> categoryList = categoryDao.findAllCategories();
               // 2.3 将集合转换为json字符串
               jsonStr = ObjectMapperUtil.getObjectMapper().writeValueAsString(categoryList);
               // 2.4 将json字符串存储到redis数据库中
               jedis.set("categoryList", jsonStr);
           } else {
               // 1.2 如果查询到则直接返回
               System.out.println("从Redis数据库中查询分类数据...");
           }
            // 关闭jedis对象：释放资源
            jedis.close();
            // 3. 将json字符串返回
            return jsonStr;
       } catch(Exception e){
           e.printStackTrace();
       }
       return null;
    }
}
