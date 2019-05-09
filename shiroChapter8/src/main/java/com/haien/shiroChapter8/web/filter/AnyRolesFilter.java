package com.haien.shiroChapter8.web.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author haien
 * @Description 自定义验证用户是否拥有指定权限中任意一个的拦截器
 * @Date 2019/3/1
 **/
public class AnyRolesFilter extends AccessControlFilter {
    //如果没有写无权限提示页面就不要写这个路径
    private String unauthorizedUrl="/unauthorized.jsp";
    private String loginUrl="/login.jsp";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws Exception {
        String[] roles=(String[])mappedValue;
        if(roles==null) return true;

        for(String role:roles){
            if(getSubject(request,response).hasRole(role)) return true;
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        Subject subject=getSubject(request,response);

        if(subject.getPrincipals()==null){
            saveRequest(request);
            WebUtils.issueRedirect(request,response,loginUrl);
        }
        else{
            if(StringUtils.hasText(unauthorizedUrl)){ //判断unauthorizedUrl是否为空
                WebUtils.issueRedirect(request,response,unauthorizedUrl);
            }
            //否则返回401未授权状态码，可以在web.xml指定返回401时显示给前端的错误页面
            else{
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        return false;
    }
}
