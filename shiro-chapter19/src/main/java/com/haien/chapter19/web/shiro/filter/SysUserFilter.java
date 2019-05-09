package com.haien.chapter19.web.shiro.filter;

import com.haien.chapter19.Constants;
import com.haien.chapter19.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SysUserFilter extends PathMatchingFilter {
    @Autowired
    private UserService userService;

    /**
     * @Author haien
     * @Description 将当前用户存入request
     * @Date 2019/3/16
     * @Param [request, response, mappedValue]
     * @return boolean
     **/
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response,
                                  Object mappedValue) throws Exception {
        String username=(String)SecurityUtils.getSubject().getPrincipal();
        request.setAttribute(Constants.CURRENT_USER,userService.findByUsername(username));
        return true;
    }
}















































