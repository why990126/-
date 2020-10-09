package com.ocean.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class MyHttpSessionListener implements HttpSessionListener {
    // 当前用户数
    private int userCounts = 0;
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // 用户数+1
        userCounts++;
        event.getSession().getServletContext().setAttribute("userCounts",userCounts);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // 用户数-1
        userCounts--;
        // 重新在servletContext中保存userCounts
        event.getSession().getServletContext().setAttribute("userCounts", userCounts);
    }
}
