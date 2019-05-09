package com.haien.chapter21.web.controller;

import com.haien.chapter21.entity.Resource;
import com.haien.chapter21.entity.User;
import com.haien.chapter21.service.ResourceService;
import com.haien.chapter21.service.UserService;
import com.haien.chapter21.web.bind.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
public class IndexController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;

    /**
     * @Author haien
     * @Description 映射登录后的主页,主要是根据用户权限查询菜单到前台显示
     * @Date 2019/3/15
     * @Param [loginUser, model]
     * @return java.lang.String
     **/
    @RequestMapping("/")
    public String index(@CurrentUser User loginUser, Model model) {
    /*@CurrentUser从request中获取当前登录对象并赋给loginUser
     （spring-config-shiro.xml中配置了SysUserFilter在每次请求的第一步将当前User存入request）*/
        //根据用户名查询权限字符串
        Set<String> permissions = userService.findPermissions(loginUser.getUsername());
        //查询跟这些权限有关的菜单
        List<Resource> menus = resourceService.findMenus(permissions);
        model.addAttribute("menus", menus);
        return "index";
    }

    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
}
