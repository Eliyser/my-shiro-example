package com.haien.chapter23.client;

import com.haien.chapter23.core.ClientSavedRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author haien
 * @Description 判断登录否的过滤链，主要是重写了保存当前地址的方法（完整形式），
 * 因为跳转到server模块的登录页面后地址的上下文改变了，等下不重写等下拼上/server/app的path肯定404
 * @Date 2019/5/7
 **/
public class ClientAuthenticationFilter extends AuthenticationFilter {
    /**
     * @Author haien
     * @Description 身份验证
     * @Date 2019/4/27
     * @Param [request, response, mappedValue]
     * @return boolean
     **/
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) {

        Subject subject = getSubject(request, response);
        return subject.isAuthenticated();
    }

    /**
     * @Author haien
     * @Description 身份验证未通过，则保存请求并重定向到登录地址/chapter23-server/login
     * @Date 2019/4/28
     * @Param [request, response]
     * @return boolean
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        //重定向地址；未登录时访问/app1，执行完该方法将跳转/server/login，
        //则此时backUrl=null，它即代表着根路径“/”
        String backUrl = request.getParameter("backUrl"); //除非当前请求特意带此参数，否则都为null
        //原地址（即当前地址）缺失协议和上下文则以当前地址的补上；
        //getDefaultBackUrl：获取默认成功地址authc.fallbackUrl并拼完整,即
        //http://localhost:9080/chapter23-app1/hello
        saveRequest(request, backUrl, getDefaultBackUrl(WebUtils.toHttp(request)));
        //重定向到登录页面
        redirectToLogin(request, response);

        return false;
    }

    /**
     * @Author haien
     * @Description 保存默认成功地址和指定返回地址到会话
     * @Date 2019/4/27
     * @Param [request, backUrl, fallbackUrl]
     * @return void
     **/
    protected void saveRequest(ServletRequest request, String backUrl, String fallbackUrl) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        //默认成功地址，即ShiroFilter中的successUrl，对于app1是/hello；
        //已补全为http://localhost:9080/chapter23-app1/hello
        session.setAttribute("authc.fallbackUrl", fallbackUrl);
        SavedRequest savedRequest = new ClientSavedRequest(httpRequest, backUrl);
        session.setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);
    }

    /**
     * @Author haien
     * @Description 获取默认返回地址authc.fallbackUrl，并拼成完整地址
     * @Date 2019/4/27
     * @Param [request]
     * @return java.lang.String
     **/
    private String getDefaultBackUrl(HttpServletRequest request) {
        String scheme = request.getScheme(); //http
        String domain = request.getServerName(); //localhost
        int port = request.getServerPort(); //80
        String contextPath = request.getContextPath(); // /chapter23-app1
        StringBuilder backUrl = new StringBuilder(scheme);
        backUrl.append("://");
        backUrl.append(domain);

        //是http协议但端口非80，实际本项目协议即为http而端口是80，所以以下俩if都不合
        if("http".equalsIgnoreCase(scheme) && port != 80) {
            backUrl.append(":").append(String.valueOf(port));
        } else if("https".equalsIgnoreCase(scheme) && port != 443) {
            backUrl.append(":").append(String.valueOf(port));
        }

        backUrl.append(contextPath); // /chapter23-app1
        backUrl.append(getSuccessUrl()); // /hello
        return backUrl.toString(); //最终拼成了http://localhost:80/chapter23-app1/hello
    }
}
