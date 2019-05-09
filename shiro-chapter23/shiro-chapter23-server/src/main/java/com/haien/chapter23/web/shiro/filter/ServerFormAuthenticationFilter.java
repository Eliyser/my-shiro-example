package com.haien.chapter23.web.shiro.filter;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author haien
 * @Description 重写 成功后返回地址 的方法
 * @Date 2019/5/8
 **/
public class ServerFormAuthenticationFilter extends FormAuthenticationFilter {

    /**
     * @Author haien
     * @Description 返回保存的地址，没有则返回默认成功地址；但因为是多项目登录，
     *              有俩默认地址，服务端的和应用的，应用的存在authc.fallbackUrl中，
     *              所以先检查session中是否有该属性，有则返回它，没有才返回服务端的
     * @Date 2019/4/26
     * @Param [request, response]
     * @return void
     **/
    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response)
            throws Exception {
        String fallbackUrl = (String) getSubject(request, response)
                .getSession().getAttribute("authc.fallbackUrl");
        if(StringUtils.isEmpty(fallbackUrl)) {
            fallbackUrl = getSuccessUrl();
        }

        //跳转到保存的原地址：先判断是否有保存的原地址，有的话返回它，没有才返回fallbackUrl
        WebUtils.redirectToSavedRequest(request, response, fallbackUrl);
    }

}
