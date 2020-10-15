package com.ocean.web.servlet;


import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;


@WebServlet
public class OnlineCountServlet implements HttpSessionListener {
    public int count = 0;
    /**
     * 当有人访问时，就会有一个session被创建，监听到session创建，count就+1
     */

    public void sessionCreated(HttpSessionEvent se) {

        count++;
        ServletContext context = se.getSession().getServletContext();
        context.setAttribute("count", new Integer(count));
        System.out.println("增加在线人数" + count);
    }

    public void sessionDestroyed(HttpSessionEvent se) {

        count--;
        ServletContext context = se.getSession().getServletContext();
        context.setAttribute("count", new Integer(count));
        System.out.println("减少在线人数" + count);
    }
}

