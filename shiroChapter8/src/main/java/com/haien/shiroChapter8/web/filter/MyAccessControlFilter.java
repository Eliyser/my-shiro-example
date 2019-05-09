package com.haien.shiroChapter8.web.filter;

import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author haien
 * @Description 以下两个方法同时被onPreHandle()调用
 * @Date 2019/3/1
 **/
public class MyAccessControlFilter extends AccessControlFilter {
    /**
     * @Author haien
     * @Description 是否允许访问，true表示允许
     * @Date 2019/3/1
     * @Param [request, response, mappedValue]
     * @return boolean
     **/
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        System.out.println("access allowed");
        return true;
    }

    /**
     * @Author haien
     * @Description 拒绝访问时是否自己处理，返回true表示自己不处理且继续执行拦截器，
     *              false表示自己已经处理了（如重定向到另一页面）
     * @Date 2019/3/1
     * @Param [request, response]
     * @return boolean
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //访问未被拒绝的话不会执行该方法
        System.out.println("访问拒绝也不自己处理，继续拦截器链的执行");
        return true;

        /*or
        ...重定向到另一页面...
        return false；
        */
    }
}