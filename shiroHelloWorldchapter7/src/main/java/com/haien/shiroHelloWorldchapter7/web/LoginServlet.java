package com.haien.shiroHelloWorldchapter7.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author haien
 * @Description 登录Servlet，搭配shiro.ini、login.jsp使用
 * @Date 2019/2/28
 **/
//相当于web.xml配置<servlet>
@WebServlet(name="loginServlet",urlPatterns = "/login") //url：http://localhost:8080/shiroHelloWorld-chapter7/login
public class LoginServlet extends HttpServlet {
    /**
     * @Author haien
     * @Description get请求时展示登录页面
     * @Date 2019/2/28
     * @Param [req, resp]
     * @return void
     **/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/login.jsp").forward(req,resp);
    }

    /**
     * @Author haien
     * @Description post请求进行登录验证
     * @Date 2019/2/28
     * @Param [req, resp]
     * @return void
     **/
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String error = null;
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            error = "用户名/密码错误";
        } catch (IncorrectCredentialsException e) {
            error = "用户名/密码错误";
        } catch (AuthenticationException e) {
            //其他错误，比如锁定，如果想单独处理请单独catch处理
            error = "其他错误：" + e.getMessage();
        }
        if(error != null) {//出错了，返回登录页面
            req.setAttribute("error", error);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        } else {//登录成功
            req.getRequestDispatcher("/jsp/loginSuccess.jsp").forward(req, resp);
        }
    }
}

























