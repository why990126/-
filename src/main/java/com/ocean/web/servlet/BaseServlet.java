package com.ocean.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 控制器的父类
 */
public class BaseServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        // 接收请求参数action
        String action = request.getParameter("action");
        try{
            // 反射技术优化代码
            // 利用反射实现自动调用UserServlet用来处理请求的方法
            // 1. 获得Class对象
            Class c = this.getClass();
            // 2. 根据方法名获得Method对象
            Method m = c.getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            // 暴露反射
            m.setAccessible(true);
            // 3. 通过Method对象调用方法 register
            m.invoke(this,request,response);
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
