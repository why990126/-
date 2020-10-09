package com.ocean.web.servlet;

import com.ocean.pojo.PageBean;
import com.ocean.pojo.Route;
import com.ocean.exception.CustomerErrorException;
import com.ocean.service.IRouteService;
import com.ocean.service.impl.RouteServiceImpl;
import com.ocean.utils.ObjectMapperUtil;
import com.ocean.utils.ServiceUtils;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 旅游路线控制器
 * http://localhost:8080/route?action=findPageBeanByCid&cid=5&curPage=1;
 */
@WebServlet(urlPatterns = "/route")
public class RouteServlet extends BaseServlet {

    // 创建业务层对象
    private IRouteService routeService = ServiceUtils.getInstance(RouteServiceImpl.class);

    /**
     * 分页查询收藏排行榜数据
     *   	参数：请求对象和响应对象
     *      返回值类型：无
     *   	实现步骤：
     *   		1. 接收请求参数：curPage
     *   		2. 调用业务层方法查询数据：PageBean对象
     *   		3. 将PageBean对象转换为json字符串
     *   		4. 将json字符串返回给浏览器
     *  http://localhost:8080/route?action=findFavoriteForRank&curPage=1&rname=双飞
     */
    private void findFavoriteForRank(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
       try{
           // 1. 接收请求参数：curPage
           int curPage = Integer.parseInt(request.getParameter("curPage"));

           // 路线名称
           String rname = request.getParameter("rname");
           // 最小金额
           String minPrice = request.getParameter("minPrice");
           // 最大金额
           String maxPrice = request.getParameter("maxPrice");

           // 需求：判断金额是否是数字，如果不是则前端弹框提示用户：金额必须是数字
           // String对象的matches方法作用：判断字符串是否符合指定的规则，符合则返回true，否则false
           // \d
           // [0-9]
           // + 1到n次
           // ? 0到1次
           // * 0到n次
           if (!StringUtils.isNullOrEmpty(minPrice)){
               if (!minPrice.matches("[1-9]\\d*")) {
                   throw new CustomerErrorException("金额必须是数字");
               }
           }

           if (!StringUtils.isNullOrEmpty(maxPrice)){
               if (!maxPrice.matches("[1-9]\\d*")) {
                   throw new CustomerErrorException("金额必须是数字");
               }
           }

           // 创建Map集合：封装查询参数
           Map<String, String> map = new HashMap<>();
           map.put("rname", rname);
           map.put("minPrice", minPrice);
           map.put("maxPrice", maxPrice);

           System.out.println("条件map ===> " + map);
           // 2. 调用业务层方法查询数据：PageBean对象
           PageBean pageBean = routeService.findPageBeanForRank(curPage,map);
           // 3. 将PageBean对象转换为json字符串
           String jsonStr = ObjectMapperUtil.getObjectMapper().writeValueAsString(pageBean);
           // 4.  将json字符串返回给浏览器
           response.getWriter().print(jsonStr);
       } catch(CustomerErrorException e){
           // 捕获自定义异常
           // 自定义json格式的字符串：{"errorMsg":"金额必须是数字"}
           String jsonStr = "{\"errorMsg\":\""+e.getMessage()+"\"}";
           response.getWriter().print(jsonStr);
       } catch (Exception e){
           throw new RuntimeException(e);
       }
    }

    /**
     * 接收请求根据rid查询路线详情信息
     * http://localhost:8080/route?action=findRouteDetail&rid=1
     * 实现步骤：
     * 	 1. 获取请求参数路线id：rid
     *   2. 调用业务层方法：根据rid查询详情数据：Route
     *   3. 将Route对象转换为json字符串
     *   4. 返回json给浏览器端
     */
    private void findRouteDetail(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        // 1. 获取请求参数路线id：rid
        int rid = Integer.parseInt(request.getParameter("rid"));
        // 2. 调用业务层方法：根据rid查询详情数据：Route
        Route route = routeService.findRouteDetailByRid(rid);
        // 3. 将Route对象转换为json字符串
        String jsonStr = ObjectMapperUtil.getObjectMapper().writeValueAsString(route);
        // 4. 返回json给浏览器端
        response.getWriter().print(jsonStr);
    }

    /**
     * 接收请求查询途牛精选数据
     * http://localhost:8080/route?action=findHeimaJx
     */
    private void findHeimaJx(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        // 1. 调用业务对象方法获得数据
        Map<String, List<Route>> map = routeService.findHeimaJx();
        // 2. 将map集合转换为json字符串
        String jsonStr = ObjectMapperUtil.getObjectMapper().writeValueAsString(map);
        // 3. 返回json给浏览器端
        response.getWriter().print(jsonStr);
    }


    /**
     * 根据分类id分页查询旅游路线
     * 	 参数：请求对象和响应对象
     * 	 返回值：无
     * 	 实现步骤：
     * 	 	1. 接收请求参数：分类id和当前页号
     * 	 	2. 创建业务层对象
     * 	 	3. 调用业务层对象的方法查询数据：PageBean对象
     * 	 	4. 将PageBean对象转换为json字符串
     * 	 	5. 将json字符串返回给浏览器端
     */
    private void findPageBeanByCid(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        // 1. 接收请求参数：分类id和当前页号
        int cid = Integer.parseInt(request.getParameter("cid"));
        int curPage = Integer.parseInt(request.getParameter("curPage"));
        // 接收请求参数rname
        String rname = request.getParameter("rname");
        // 2. 调用业务层对象的方法查询数据：PageBean对象
        PageBean pageBean = routeService.findRoutesByiCid(cid, curPage,rname);
        // 3. 将PageBean对象转换为json
        String jsonStr = ObjectMapperUtil.getObjectMapper().writeValueAsString(pageBean);
        // 4. 将json字符串返回给浏览器端
        response.getWriter().print(jsonStr);
    }

}
