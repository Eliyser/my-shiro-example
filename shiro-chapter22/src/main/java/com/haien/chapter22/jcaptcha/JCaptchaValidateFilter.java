package com.haien.chapter22.jcaptcha;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author haien
 * @Description 验证验证码的shiro的过滤器
 * @Date 2019/4/19
 **/
public class JCaptchaValidateFilter extends AccessControlFilter {
    //是否开启验证码支持
    private boolean jcaptchaEnabled=true;
    //前台提交的验证码参数名
    private String jcaptchaParam="jcaptchaCode";
    //验证失败后存储到的属性名
    private String failureKeyAttribute="shiroLoginFailure";

    public void setJcaptchaEnabled(boolean jcaptchaEnabled) {
        this.jcaptchaEnabled = jcaptchaEnabled;
    }
    public void setJcaptchaParam(String jcaptchaParam) {
        this.jcaptchaParam = jcaptchaParam;
    }
    public void setFailureKeyAttribute(String failureKeyAttribute) {
        this.failureKeyAttribute = failureKeyAttribute;
    }

    /**
     * @Author haien
     * @Description 验证验证码
     * @Date 2019/4/19
     * @Param [request, response, mappedValue]
     * @return boolean
     **/
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws Exception {
        //1. 设置验证码是否开启属性，页面据此决定是否显示验证码
        request.setAttribute("jcaptchaEnabled",jcaptchaEnabled);
        //将HttpServlet的request转为HttpServlet类型
        HttpServletRequest httpServletRequest=WebUtils.toHttp(request);

        //2. 判断验证码是否禁用|不是由表单提交的
        if(jcaptchaEnabled==false || ! "post".equalsIgnoreCase(httpServletRequest.getMethod())){
            //则不用验证，直接结束
            return true;
        }

        //3. 验证 验证码,正确则返回true，否则false
        return JCaptcha.validateResponse(httpServletRequest,
                httpServletRequest.getParameter(jcaptchaParam));
    }

    /**
     * @Author haien
     * @Description 验证失败处理
     * @Date 2019/4/19
     * @Param [request, response]
     * @return boolean
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        //若验证失败，则存储failureKeyAttribute属性；可通过getFailureKeyAttribute()获取
        request.setAttribute(failureKeyAttribute,"jCaptcha.error");
        return true;
    }
}






























