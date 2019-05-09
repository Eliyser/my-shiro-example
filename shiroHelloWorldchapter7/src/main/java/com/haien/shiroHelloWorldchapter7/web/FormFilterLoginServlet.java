package com.haien.shiroHelloWorldchapter7.web;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author haien
 * @Description 搭配基于表单的拦截器身份验证shiro-formfilterlogin、formfilterlogin.jsp使用
 * @Date 2019/2/28
 **/
@WebServlet(name = "formFilterLoginServlet", urlPatterns = "/formfilterlogin")
public class FormFilterLoginServlet extends HttpServlet {
    /**
     * @Author haien
     * @Description get请求的话FormAuthenticationFilter不做任何处理直接结束当前过滤链，
     *              进入下一过滤链，应该是Spring自己的过滤链了，应该就是分发给controller处理，
     *              所以这里应该是映射登录页面才对，不过交给doPost做也获取不到shiroLoginFailure，
     *              也是直接返回登录页面了
     * @Date 2019/3/15
     * @Param [req, resp]
     * @return void
     **/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * @Author haien
     * @Description 身份验证和成功跳转已被处理，这里只处理失败情况
     * @Date 2019/2/28
     * @Param [req, resp]
     * @return void
     **/
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String errorClassName = (String)req.getAttribute("shiroLoginFailure"); //异常类型名
        if(UnknownAccountException.class.getName().equals(errorClassName)) {
            req.setAttribute("error", "用户名/密码错误");
        } else if(IncorrectCredentialsException.class.getName().equals(errorClassName)) {
            req.setAttribute("error", "用户名/密码错误");
        } else if(errorClassName != null) {
            req.setAttribute("error", "未知错误：" + errorClassName);
        }
        req.getRequestDispatcher("/jsp/formfilterlogin.jsp").forward(req, resp); //返回登录页面
    }
}
