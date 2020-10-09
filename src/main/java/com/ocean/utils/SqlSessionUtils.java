package com.ocean.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 回顾MyBatis操作数据库的步骤
    1. 加载主配置文件：sqlMapConfig.xml
    2. 创建SqlSessionFactoryBuilder对象
    3. 创建SqlSessionFactory对象

    4. 获得SqlSession对象
    5. 获得接口代理对象

    6. 通过接口代理对象调用方法执行数据库操作
    7. 提交事务并关闭SqlSession

 * SqlSession工具类
 */
public class SqlSessionUtils {

    private static SqlSessionFactory sqlSessionFactory;
    // 创建线程局部对象：存储sqlSession对象
    private static ThreadLocal<SqlSession> local = new ThreadLocal<>();
    static {
        try{
            // 1. 加载主配置文件：sqlMapConfig.xml
            InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
            // 2. 创建SqlSessionFactoryBuilder对象
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            // 3. 创建SqlSessionFactory对象
            sqlSessionFactory = builder.build(in);
            // 4. 关闭流释放资源
            in.close();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    // 4. 获得SqlSession对象
    public static SqlSession openSession(){
        // 1. 从local中获得sqlSession
        SqlSession sqlSession = local.get();
        // 2. 判断是否有值，
        if (sqlSession == null) {
            // 3. 从会话工厂获得
            sqlSession = sqlSessionFactory.openSession();
            // 将sqlsession存储到local中
            local.set(sqlSession);
        }
        // 4. 将sqlSession对象返回
        return sqlSession;
    }

    // 5. 获得接口代理对象
    public static <T> T getMapper(Class<T> interfaceClass){
        // 返回接口代理对象
        return (T)Proxy.newProxyInstance(
                SqlSessionUtils.class.getClassLoader(),
                new Class[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 获得sqlSession对象
                        SqlSession sqlSession = openSession();
                        // 获得接口代理对象
                        T mapper = sqlSession.getMapper(interfaceClass);
                        // 调用真实对象方法
                        Object result = method.invoke(mapper, args);
                        return result;
                    }
                });
    }


    /**
     * 提交事务并关闭连接
     */
    public static void commitAndClose() {
        // 获得sqlSession
        SqlSession sqlSession = openSession();
        if (sqlSession != null){
            System.out.println("提交事务并关闭连接 = " + sqlSession.hashCode());
            // 提交事务
            sqlSession.commit();
            // 关闭连接
            sqlSession.close();
            // 从local中移出sqlSession
            local.remove();
        }

    }

    /**
     * 回滚事务并关闭连接
     */
    public static void rollbackAndClose() {
        // 获得sqlSession
        SqlSession sqlSession = openSession();
        if (sqlSession != null) {
            System.out.println("回滚事务并关闭连接 = "  + sqlSession.hashCode());
            // 提交事务
            sqlSession.rollback();
            // 关闭连接
            sqlSession.close();
            // 从local中移出sqlSession
            local.remove();
        }
    }
}
