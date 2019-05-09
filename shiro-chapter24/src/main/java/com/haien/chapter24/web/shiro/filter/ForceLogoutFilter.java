package com.haien.chapter24.web.shiro.filter;

import com.haien.chapter24.Constants;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author haien
 * @Description 强制退出拦截器
 * @Date 2019/5/9
 **/
public class ForceLogoutFilter extends AccessControlFilter {
    /**
     * @Author haien
     * @Description 判断当前会话是否需要强制退出
     * @Date 2019/5/9
     * @Param [request, response, mappedValue]
     * @return boolean
     **/
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws Exception {
        Session session=getSubject(request,response).getSession(false);

        //当前无会话，直接允许访问
        if(null==session) return true;

        return session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY) == null;
    }

    /**
     * @Author haien
     * @Description 强制退出
     * @Date 2019/5/9
     * @Param [request, response]
     * @return boolean
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        try {
            //强制退出
            getSubject(request, response).logout();
        } catch (Exception e) {
            //ignore
        }

        //添加forceLogout参数
        String loginUrl=getLoginUrl() +
                (getLoginUrl().contains("?") ? "&":"?")+"forceLogout=1";
        WebUtils.issueRedirect(request,response,loginUrl);

        return false;
    }
}












































