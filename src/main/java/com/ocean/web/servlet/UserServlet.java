package com.ocean.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocean.pojo.User;
import com.ocean.exception.CustomerErrorException;
import com.ocean.service.IUserService;
import com.ocean.service.impl.UserServiceImpl;
import com.ocean.utils.ObjectMapperUtil;
import com.ocean.utils.ServiceUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 UserServlet控制器
 需求：实现一个控制器可以接收浏览器发送的多个请求
 实现：浏览器发送请求时传递多一个参数action，关联控制器的方法名==>用来处理该请求的方法
 http://localhost:8080/user?action=register ==> 调用register方法
 http://localhost:8080/user?action=login ==> 调用login方法

 问：当Servlet接收到请求时会先触发哪个方法？
    A：doGet
    B：doPost
    C：service
 */
@WebServlet(urlPatterns = "/user")
public class UserServlet extends BaseServlet {
    // 4. 创建业务层对象：代理对象
    private IUserService userService = ServiceUtils.getInstance(UserServiceImpl.class);
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        this.doPost(request, response);
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        // 1. 接收请求参数action
//        String action = request.getParameter("action");
//
//        // 2. 判断是否是注册
//        if ("register".equals(action)){
//            // 调用register方法
//            register(request, response);
//        } else if ("login".equals(action)){
//            // 调用login方法
//            login(request,response);
//        } else if("getLoginUserInfo".equals(action)){
//            // 调用getLoginUserInfo方法
//            getLoginUserInfo(request, response);
//        } else if ("logout".equals(action)){
//            // 调用logout方法
//            logout(request, response);
//        }
//       try{
//           // 反射技术优化代码
//           // 利用反射实现自动调用UserServlet用来处理请求的方法
//           // 1. 获得Class对象
//           Class c = this.getClass();
//           // 2. 根据方法名获得Method对象
//           Method m = c.getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
//           // 3. 通过Method对象调用方法
//           m.invoke(this,request,response);
//       } catch(Exception e){
//           e.printStackTrace();
//       }
//    }


    /**
     * 退出方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    /**
     * 实现用户退出
     */
    private void logout(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
//        // 1. 获得会话域对象
//        HttpSession session = request.getSession();
//        // 2. 销毁session
//        session.invalidate();
        // 1. 删除会话域的用户信息
        request.getSession().removeAttribute("loginUser");
        // 3. 跳转到登录界面
        response.sendRedirect("login.html");
    }
    /**
     * 从会话域中获得登录用户的信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLoginUserInfo(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
//        // 1.1 获得会话域对象
//        HttpSession session = request.getSession();
//        // 1.2 从会话域中获得用户对象
//        User user = (User) session.getAttribute("loginUser");
        // 1. 从会话域获取用户信息
        User user = (User) request.getSession().getAttribute("loginUser");
//        // 1.3 将用户对象转换为json字符串
//        String jsonStr = ObjectMapperUtil.getObjectMapper().writeValueAsString(user);
        // 2. 将用户对象转换JSON字符串
        String jsonStr = new ObjectMapper().writeValueAsString(user);
        // 1.4 将json字符串返回给浏览器
        response.getWriter().println(jsonStr);
    }

    /**
     * 实现登录功能
     * @param request
     * @param response
     */
    private void login(HttpServletRequest request, HttpServletResponse response)  throws CustomerErrorException, IOException{
       try{
           // 1. 接收请求参数：验证码，用户名，密码
           String check = request.getParameter("check");
           String username = request.getParameter("username");
           String password = request.getParameter("password");

           // 2. 判断验证码是否正确
           // 2.1 获得会话域验证码字符串
          // String verCode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
           String code = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
           // 2.2 判断验证码是否正确
           if (check == null || !check.equalsIgnoreCase(code)) {
               throw new CustomerErrorException("验证码错误"); // error
           }
           // 3. 调用业务层方法执行登录获取结果：User对象
           User user = userService.login(username, password);
           // 4. 将用户信息存储到会话域中
           request.getSession().setAttribute("loginUser", user);
           // 5. 返回结果给浏览器
           response.getWriter().print(true);
       }  catch(CustomerErrorException e){
           // 捕获自定义异常：用户可以解决
           response.getWriter().println(e.getMessage());
       }  catch(Exception e){
           e.printStackTrace();
           throw new RuntimeException(e);
       }
    }
//
//           if (check == null || check.trim().length() == 0) {
//               throw new CustomerErrorException("验证码不能为空");
//           }
//           if (!verCode.equalsIgnoreCase(check)){
//               throw new CustomerErrorException("验证码错误");
//           }
//           // 3. 调用业务层对象方法执行登录操作
//           User user = userService.login(username, password);
//           // 将用户对象存储到会话域中：其他页面需要使用用户数据从会话域中获取
//           request.getSession().setAttribute("loginUser", user);
//           // 4. 返回结果给浏览器端
//           response.getWriter().print("true");
//       } catch(CustomerErrorException e){
//           e.printStackTrace();
//           response.getWriter().println(e.getMessage());
//       } catch(Exception e){
//           e.printStackTrace();
//           throw new RuntimeException(e);
//       }
//    }

    /**
     * 实现注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void register(HttpServletRequest request, HttpServletResponse response) throws CustomerErrorException, IOException {
        try{
            // 1. 判断验证码是否正确
            // 1.1 获取用户输入的验证码
            String check = request.getParameter("check");
            // 1.2 获取会话域中的验证码
            String code = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
            if (check == null || !check.equalsIgnoreCase(check)){
                // 验证码不正确
                throw new CustomerErrorException("验证码错误");
            }
            // 模拟系统异常
            //  System.out.println(100/0);
            // 1. 接收请求参数：用户注册信息
            Map<String, String[]> map = request.getParameterMap();
            // 2. 创建用户对象：封装用户信息
            User user = new User();
            BeanUtils.populate(user, map);
            // 3. 调用业务层方法：实现用户注册
            boolean result = userService.registerUser(user); // 用户名存在
            // 4. 返回结果给浏览器
            response.getWriter().print(result); // false或true
        } catch(CustomerErrorException e){
            // 捕获自定义异常：用户可以解决
            response.getWriter().println(e.getMessage());
        } catch(Exception e){
            // 系统异常：用户不能解决的，前端给一个友好提示就可：服务器忙...
            // 触发前端的error回调函数即可
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
//        try{
//           // 0. 获得请求参数的验证码
//           String code = request.getParameter("check");
//           // 从会话域中获得验证码
//           String verCode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
//           if (code == null || code.trim().length() == 0) {
//               // 抛出自定义异常
//               throw new CustomerErrorException("验证码不能为空");
//           }
//           // 判断验证码是否正确
//           if(!verCode.equalsIgnoreCase(code)){
//               // 抛出自定义异常
//               throw new CustomerErrorException("验证码错误");
//           }
//
//           // 模拟系统异常
//           // System.out.println(100/0);
//
//           // 1. 接收请求参数：Map
//           Map<String, String[]> map = request.getParameterMap();
//           // 2. 创建User对象：用来封装请求参数
//           User user = new User();
//           // 3. 使用BeanUtils工具类封装请求参数到User对象
//           BeanUtils.populate(user, map);
//           // 5. 调用业务层对象方法实现用户注册
//           boolean result = userService.registerUser(user);
//           // 6. 返回注册结果给浏览器
//           response.getWriter().print(result);
//       } catch(CustomerErrorException e){
//           // 捕获到自定义异常
//           // 获得自定义异常信息
//           String msg = e.getMessage();
//           // 返回异常信息给浏览器：用户可以解决的异常
//           response.getWriter().println(msg);
//           // 将异常信息输出到控制台
//           e.printStackTrace();
//       } catch (Exception e){
//           // 捕获到系统异常：浏览器端给友好提示：服务器忙...
//           // 将异常信息输出到控制台
//           e.printStackTrace();
//           // 将异常抛出浏览器端：触发ajax异步请求的错误回调：服务器忙...
//           throw new RuntimeException(e);
//       }
//    }
//
//}
//

