package com.haien.shiroChapter8.web.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author haien
 * @Description 自定义基于表单的拦截器
 * @Date 2019/3/1
 **/
public class FormLoginFilter extends PathMatchingFilter {
    private String loginUrl="/login.jsp";
    private String successUrl="/";

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response,
                                  Object mappedValue) throws Exception {

        //已登陆过，直接返回
        if(SecurityUtils.getSubject().isAuthenticated()) return true;

        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse resp=(HttpServletResponse)response;
        //登录请求（登录页面|登录验证）
        if(isLoginRequest(req)){
            if("post".equalsIgnoreCase(req.getMethod())){ //post，即为登录验证请求
                //登录
                boolean loginSuccess=login(req);
                //登录成功，重定向到之前保存的请求，没有则默认成功页面；失败自会在login()内抛异常
                if(loginSuccess) return redirectToSuccessUrl(req,resp);
            }

            //继续过滤链
            return true;
        }
        //非登录请求
        else{
            saveRequestAndRedirectToLogin(req,resp);
            return false;
        }
    }

    public boolean redirectToSuccessUrl(HttpServletRequest req,HttpServletResponse reps)
            throws IOException{
        WebUtils.redirectToSavedRequest(req,reps,successUrl);
        return false;
    }

    public void saveRequestAndRedirectToLogin(HttpServletRequest req,HttpServletResponse resp)
            throws IOException{
        WebUtils.saveRequest(req);
        WebUtils.issueRedirect(req,resp,loginUrl);
    }

    public boolean login(HttpServletRequest req){
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        try{
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username,password));
        }catch(Exception e){
            req.setAttribute("shiroLoginFailure",e.getClass());
            return false;
        }

        return true;
    }

    public boolean isLoginRequest(HttpServletRequest req){
        return pathsMatch(loginUrl,WebUtils.getPathWithinApplication(req));
    }
}
