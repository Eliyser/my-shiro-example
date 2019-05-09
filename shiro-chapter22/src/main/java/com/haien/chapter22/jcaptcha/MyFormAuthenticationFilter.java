package com.haien.chapter22.jcaptcha;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author haien
 * @Description 自定义身份验证拦截器，如果验证码拦截器失败了则不用执行身份验证了
 * @Date 2019/4/19
 **/
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //若为登录请求，无论登录与否，直接进入onAccessDenied
        if(isLoginRequest(request,response)) return false;

        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response,
                                     Object mappedValue) throws Exception {
        //失败则直接返回，不用身份验证了
        if(request.getAttribute(getFailureKeyAttribute()) != null) return true;

        return super.onAccessDenied(request,response,mappedValue);
    }
}

































