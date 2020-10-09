package com.ocean.web.servlet;

import com.ocean.service.ICategoryService;
import com.ocean.service.impl.CategoryServiceImpl;
import com.ocean.utils.ServiceUtils;
import com.ocean.web.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 创建分类请求的控制器：CategoryServlet 继承 BaseServlet
 * 分类控制器
 * http://localhost:8080/category?action=findAllCategories
 */
@WebServlet(urlPatterns = "/category")
public class CategoryServlet extends BaseServlet {
    // 1. 创建业务层对象
    private ICategoryService categoryService = ServiceUtils.getInstance(CategoryServiceImpl.class);
    /**
     * 2、定义方法：接收浏览器查询分类数据的请求
     * 	 参数：请求对象和响应对象
     * 	 返回值：无，因为通过响应对象响应数据给浏览器
     * 	 实现步骤：
     * 	    1. 创建业务层对象
     * 	    2. 调用业务层对象的方法：获得分类数据
     * 	    3. 将分类数据响应给浏览器端
     * */
    private void findAllCategories(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {

//        // 1. 创建业务层对象
//        ICategoryService categoryService = ServiceUtils.getInstance(CategoryServiceImpl.class);
        // 2. 调用方法查询分类数据
        String categories = categoryService.findAllCategories();
        // 3. 响应分类数据给浏览器
        response.getWriter().println(categories);
    }
}
