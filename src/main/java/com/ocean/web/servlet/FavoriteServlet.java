package com.ocean.web.servlet;

import com.ocean.pojo.PageBean;
import com.ocean.pojo.User;
import com.ocean.service.impl.FavoriteServiceImpl;
import com.ocean.service.IFavoriteService;
import com.ocean.utils.ObjectMapperUtil;
import com.ocean.utils.ServiceUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 控制器
 */
@WebServlet(urlPatterns = "/favorite")
public class FavoriteServlet extends BaseServlet {
    // 创建业务层对象
    private IFavoriteService favoriteService = ServiceUtils.getInstance(FavoriteServiceImpl.class);


    /**
     *    定义方法接收请求：分页查询收藏记录
     * 	  参数：请求对象和响应对象
     *    返回值类型：无
     *    实现步骤：
     *   		1. 调用业务层对象的方法查询数据：PageBean对象
     *   		2. 将PageBean对象转换为json字符串
     *   		3. 将json字符串返回给浏览器端
     *
     *   http://localhost:8080/favorite?action=findPageBeanFavorite&curPage=1
     */
    private void findPageBeanFavorite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. 接收请求参数：curPage
        int curPage = Integer.parseInt(request.getParameter("curPage"));
        // 2. 从会话域中获得用户id：uid
        User user = (User) request.getSession().getAttribute("loginUser");
        int uid = user.getUid();
        // 3. 调用业务层对象的方法查询数据：PageBean对象
        PageBean pageBean = favoriteService.findPageBeanFavorite(uid, curPage);
        // 4. 将PageBean对象转换为json字符串
        String jsonStr = ObjectMapperUtil.getObjectMapper().writeValueAsString(pageBean);
        // 5. 将json字符串返回给浏览器端
        response.getWriter().print(jsonStr);
    }

    /**
     * 添加收藏路线
     */
    private void addFavorite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. 接收请求参数：rid
        int rid = Integer.parseInt(request.getParameter("rid"));
        // 2. 从会话域中获得用户id
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            // 3. 如果没有登录则返回：没有登录
            response.getWriter().print("没有登录");
            return;
        }
        // 4. 调用业务层方法执行添加收藏：并获得最新收藏数量
        int count = favoriteService.addFavorite(loginUser.getUid(), rid);
        // 5. 将业务返回的结果响应给浏览器端
        response.getWriter().print(count);
    }
    /**
     * 添加收藏路线
     */
    private void deleteFavorite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. 接收请求参数：rid
        int rid = Integer.parseInt(request.getParameter("rid"));

        int count = favoriteService.deleteFavorite(rid);
        // 5. 将业务返回的结果响应给浏览器端
        response.getWriter().print(count);
    }


    /**
     *  判断用户是否收藏了旅游路线
     */
    private void isFavorite(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        // 1. 接收请求参数：rid
        int rid = Integer.parseInt(request.getParameter("rid"));
        // 2. 从会话域中获得用户id
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            // 2.1 返回false给浏览器对象即可：如果没有登录，则uid就会是null，在用户没有登录，可以认为没有收藏了
            response.getWriter().print("false");
            return;
        }
        // 2.2 获得用户id
        int uid = loginUser.getUid();
        // 3. 调用业务层方法：传递uid和rid，得到结果：true 收藏了  false：没收藏
        boolean result = favoriteService.isFavorite(uid, rid);
        // 4. 将结果返回给浏览器
        response.getWriter().print(result);
    }
}
