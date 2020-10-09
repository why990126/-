package com.ocean.service.impl;

import com.ocean.dao.IUserDao;
import com.ocean.exception.CustomerErrorException;
import com.ocean.service.IUserService;
import com.ocean.pojo.User;
import com.ocean.utils.Md5Util;
import com.ocean.utils.SqlSessionUtils;

/**
 * 业务层
 */
public class UserServiceImpl implements IUserService {

    // 1. 创建数据访问层对象
    private IUserDao userDao = SqlSessionUtils.getMapper(IUserDao.class);
    /**
     * 1.定义方法：实现注册功能
     *   参数：用户对象：User
     *   返回值类型：返回boolean，true代表注册成功，false代表注册失败
     *   实现步骤：
     *   	1. 创建数据访问层对象
     *      2. 查询用户名是否存在
     *      3. 保存用户信息：实现注册
     */
    @Override
    public boolean registerUser(User user){
        // 1. 获得用户名
        String username = user.getUsername(); // jack
        // 1.1 判断用户名是否null或空串：因为浏览器时可以禁用js代码的，因此前端判空可能会失效
        if (username == null || username.trim().length() == 0) {
            throw new CustomerErrorException("用户名不能为空");
        }
        // 2. 查询用户名是否存在
        User u = userDao.findByUsername(username); // jack
        // 2.1 判断u是否不为null
        if (u != null){
            // 2.2 说明用户名已经存在，则返回信息给浏览器端
            // 返回false前端页面只知道注册失败了，无法告知失败的原因是什么
            // 问：如何返回结果能够告诉浏览器注册失败并知道失败的原因？
            // 答：抛出异常：因为抛出异常既可以结束当前方法，又可以携带异常信息。
            // return false;
            // 为了和系统异常进行区分，我们采用抛出自定义异常
            // 原因：系统异常一般描述的异常是用户无法解决的异常，比如服务器出问题，前端页面给出友好提示即可
            // 自定义异常是用来描述用户可以解决的异常：比如用户名已经存在，密码错误，验证码错误，用户名不能为null
            throw new CustomerErrorException("用户名已经存在");
        }

        // 为什么要加密? 为了防止数据库管理人员或开发人员泄露用户密码==> md5
        // md5特点：不可逆的加密算法，相同内容每次md5结果都是一样的
        // 123456 ==> md5 ==> fasdfafasdf
        // 123456 ==> md5 ==> fasdfafasdf
        // fasdfafasdf ==> 123456

        // 对密码进行加密
        String md5Pwd = Md5Util.getMD5(user.getPassword());
        user.setPassword(md5Pwd);

        // 3. 保存用户信息：实现注册
        return userDao.saveUser(user) == 1;
    }

    /**
     * 1. 定义方法：实现用户登录
     * 	 参数：用户名和密码
     * 	 返回值：User对象
     * 	 实现步骤：
     * 	 	 1. 创建数据访问层对象
     * 	 	 2. 根据用户名查询用户对象
     * 	 	 3. 判断密码是否正确
     * 	 	 4. 返回结果
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public User login(String username, String password) { // 123
        // 2. 根据用户名查询用户对象
        User user = userDao.findByUsername(username); // jack123
        if (user == null) {
            // 抛出自定义异常
            throw new CustomerErrorException("用户名不存在");
        }
        // 3. 判断密码是否正确
        // 3.1 先对用户传递的密码进行md5
        String md5Pwd = Md5Util.getMD5(password);
        // 3.2 获得数据库的用户密码
        String pwd = user.getPassword();
        if (!md5Pwd.equals(pwd)) {
            // 抛出自定义异常
            throw new CustomerErrorException("密码错误");
        }
        // 4. 返回用户对象
        return user;
    }
}
