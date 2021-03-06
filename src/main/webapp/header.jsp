<%--
  Created by IntelliJ IDEA.
  User: skrskr
  Date: 2020/10/9
  Time: 20:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!-- 头部 start -->
<header id="header">
    <div class="top_banner">
        <img src="images/top_banner.png" width="100%" alt="">
    </div>
    <div class="shortcut">
        <div  class="online"
              style="
                width: 120px;
                font-size: 12px;
                height: 50px;
                line-height: 36px;
                float: right;">
            当前在线人数:${count}
        </div>
        <!-- 未登录状态  -->
        <div class="login_out">
            <a href="login.html">登录</a>
            <a href="register.html">注册</a>
        </div>
        <!-- 登录状态  -->
        <div class="login">
            <span>欢迎回来，admin</span>
            <a href="myfavorite.html" class="collection">我的收藏</a>
            <a href="user?action=logout">退出</a>
        </div>
    </div>
    <div class="header_wrap">
        <div class="topbar">
            <div class="logo">
                <a href="/"><img src="images/logo.png" alt=""></a>
            </div>
            <div class="search">
                <input name="rname"
                       id="rname" type="text"
                       placeholder="请输入旅游路线名称"
                       class="search_input" autocomplete="off">
                <a href="javascript:searchRoute();" class="search-button">搜索</a>

                <script>
                    // 监听搜索按钮点击
                    function searchRoute() {
                        // 获得文本输入框的值
                        var rname = $("#rname").val();
                        //alert(rname)
                        // 发送请求根据条件搜索旅游路线
                        showData(1,rname);
                    }
                </script>
            </div>
            <div class="hottel">
                <div class="hot_pic">
                    <img src="images/telephone.png" alt="">
                </div>
            </div>
        </div>
    </div>
</header>
<!-- 头部 end -->
<!-- 首页导航 -->
<div class="navitem">
    <ul class="nav">
        <li class="nav-active"><a href="index.html">首页</a></li>
        <li><a href="route_list.html">门票</a></li>
        <li><a href="route_list.html">酒店</a></li>
        <li><a href="route_list.html">香港车票</a></li>
        <li><a href="route_list.html">出境游</a></li>
        <li><a href="route_list.html">国内游</a></li>
        <li><a href="route_list.html">港澳游</a></li>
        <li><a href="route_list.html">抱团定制</a></li>
        <li><a href="route_list.html">全球自由行</a></li>
        <li><a href="favoriterank.html">收藏排行榜</a></li>
    </ul>
</div>
</body>
<script src="js/jquery-3.3.1.min.js"></script>
<script>
    // 问：什么时候发送请求获取用户登录数据？
    // 答：在网页加载完毕时发送请求
    $(function () {
        // 隐藏未登录状态和登录状态的div
        $(".login_out").hide();
        $(".login").hide();
        // 发送请求获取用户登录数据
        $.get({
            url:"user",// 请求地址
            data:"action=getLoginUserInfo", // 请求参数
            dataType:"json", // 返回数据类型
            success:function (user) { // 成功回调
                // user 就是一个json对象
                // 判断user是否为null
                if(user == null) {
                    // 没有登录
                    $(".login_out").show();
                    $(".login").hide();
                } else {
                    // 已经登录
                    // 显示登录用户名
                    $(".login span").html("欢迎回来，" + user.username);
                    $(".login_out").hide();
                    $(".login").show();
                }
            },
            error:function () {
                alert("无登录");
            }
        });


        // 发送请求到服务器获得旅游分类数据
        $.get({
            url:"category" , // 请求地址
            data:"action=findAllCategories", // 请求参数
            dataType:"json", // 服务器返回数据类型
            success:function (categoryList) {
                // categoryList 就是 json 数组
                // 更新页面分类数据
                // 定义变量：用来拼接li元素
                var html = "<li class=\"nav-active\">" +
                    "<a style='color: white' href=\"index.html\">首页</a>" +
                    "</li>";
                // 遍历json数组
                $(categoryList).each(function (index,category) {
                    // 拼接li元素
                    html += "<li><a style='color: white' href=\"route_list.html?cid="+category.cid+"\">"+category.cname+"</a></li>";
                });
                // 拼接最后一个li元素：收藏排行榜
                html += "<li><a style='color: white' href=\"favoriterank.html\">收藏排行榜</a></li>";
                // 设置ul标签体内容
                $(".navitem .nav").html(html);
            },
            error:function () {
                alert("无分类")
            }
        });
    });
</script>
</html>
