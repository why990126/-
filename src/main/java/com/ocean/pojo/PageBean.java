package com.ocean.pojo;


import java.util.List;

/**
 * 封装分页数据
 */
public class PageBean<T> {
    private List<T> dataList;       //当前页数据列表
    private int firstPage;          //首页
    private int prePage;            //上一页
    private int curPage;            //当前页
    private int nextPage;           //下一页
    private int totalPage;          //总页数
    private int count;              //总条数
    private int pageSize;           //每页大小

    // 1. 哪些成员变量的值需要前端传递？ curPage, pageSize
    // 2. 哪些成员变量的值需要从数据库查询？dataList count
    // 3. 哪些成员变量的值可以在当前PageBean中进行封装？ firstPage  prePage nextPage  totalPage
    // 返回PageBean对象
    public static <T> PageBean getPageBean(List<T> dataList,int count,int curPage,int pageSize){
        // 创建PageBean对象
        PageBean<T> pageBean = new PageBean<>();

        // 封装成员变量值
        pageBean.dataList = dataList;
        pageBean.count = count;
        pageBean.curPage = curPage;
        pageBean.pageSize = pageSize;

        pageBean.firstPage = 1;
        pageBean.prePage = curPage - 1;
        pageBean.nextPage = curPage + 1;
        pageBean.totalPage = count % pageSize == 0 ? count / pageSize: count / pageSize + 1;

        // 返回PageBean对象
        return pageBean;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
