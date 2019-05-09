package com.haien.chapter20.filter;

import com.haien.chapter20.Constants;
import com.haien.chapter20.realm.StatelessToken;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author haien
 * @Description 代替FormAuthenticationFilter做登录拦截，因为要截获的参数不止表单参数;
 *              实际不能说是登录拦截，而应该是认证，因为不管当前Subject是否已登录，每次请求都要重新登录
 * @Date 2019/4/8
 **/
public class StatelessAuthcFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    /**
     * @Author haien
     * @Description 获取客户端传入的用户名、参数和消息摘要，生成StatelessToken，然后交给realm登录
     *              （实际是认证）
     * @Date 2019/4/8
     * @Param [request, response]
     * @return boolean
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        //1. 获取客户端生成的消息摘要
        String clientDigest=request.getParameter(Constants.PARAM_DIGEST);
        //2. 获取客户端传入的用户身份
        String username=request.getParameter(Constants.PARAM_USERNAME);
        //3. 获取客户端请求的参数列表
        Map<String,String[]> params=new HashMap<>(request.getParameterMap());
        params.remove(Constants.PARAM_DIGEST);
        //4. 封装成无状态Token
        StatelessToken token=new StatelessToken(username,params,clientDigest);
        //5. 委托给realm进行登录，准确说是认证
        try {
            getSubject(request, response).login(token);
        }catch (Exception e){
            e.printStackTrace();
            //6. 认证失败处理
            onLoginFail(response);
            return false;
        }

        //认证成功返回true，执行其他过滤链
        return true;
    }

    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse=(HttpServletResponse)response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("login error");
    }
}




















