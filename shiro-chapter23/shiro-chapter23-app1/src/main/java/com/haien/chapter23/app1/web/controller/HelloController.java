package com.haien.chapter23.app1.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

    /**
     * @Author haien
     * @Description 映射成功页面
     * @Date 2019/5/4
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping("/hello")
    public String hello() {
        return "success";
    }

    /**
     * @Author haien
     * @Description 成功页面“设置会话属性”提交地址，属性为一键值对，将其存入会话中
     * @Date 2019/5/4
     * @Param [key, value]
     * @return java.lang.String
     **/
    @RequestMapping(value = "/attr", method = RequestMethod.POST)
    public String setAttr(@RequestParam("key") String key,
                          @RequestParam("value") String value){
        SecurityUtils.getSubject().getSession().setAttribute(key, value);
        return "success";
    }

    /**
     * @Author haien
     * @Description 同上，不过提交方式为get
     * @Date 2019/5/4
     * @Param [key, model]
     * @return java.lang.String
     **/
    @RequestMapping(value = "/attr", method = RequestMethod.GET)
    public String getAttr(
            @RequestParam("key") String key, Model model) {
        model.addAttribute("value", SecurityUtils.getSubject().getSession()
                .getAttribute(key));
        return "success";
    }

    /**
     * @Author haien
     * @Description 模拟role1角色请求成功页面
     * @Date 2019/5/4
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping("/role1")
    @RequiresRoles("role1") //需要role1角色
    public String role1() {
        return "success";
    }

}
