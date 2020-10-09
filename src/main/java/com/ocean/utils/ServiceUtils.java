package com.ocean.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 业务层工具类
 * 目标：为业务层对象生成代理对象
 */
public class ServiceUtils {
    /**
     * 为业务类创建代理对象并返回
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> clazz) {
        // 1. 创建动态代理对象
        T proxy = (T)Proxy.newProxyInstance(
                clazz.getClassLoader(), // 真实对象的类加载器
                clazz.getInterfaces(), // 真实对象实现的接口
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                       try{
                           // 1. 创建业务层对象：真实对象
                           T obj = clazz.getConstructor().newInstance();
                           // 2. 调用真实对象的方法
                           Object result = method.invoke(obj, args);
                           // 3. 提交事务或回滚事务
                           SqlSessionUtils.commitAndClose();
                           return result;
                       } catch(InvocationTargetException e){
                           // 获得异常对象e封装的异常对象
                           Throwable customerException = e.getTargetException();
                           // 4. 回滚事务并关闭连接
                           SqlSessionUtils.rollbackAndClose();
                           // 抛出自定义异常
                           throw customerException;
                       } catch(Exception e){
                           // 4. 回滚事务并关闭连接
                           SqlSessionUtils.rollbackAndClose();
                           // 抛出异常
                           throw e;
                       }
                    }
                });
        // 2. 返回动态代理对象
        return proxy;
    }

}
