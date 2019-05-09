package com.haien.chapter21.web.controller;

import com.haien.chapter21.entity.User;
import com.haien.chapter21.service.UserRunAsService;
import com.haien.chapter21.service.UserService;
import com.haien.chapter21.web.bind.annotation.CurrentUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/runas")
public class RunAsController {

    @Autowired
    private UserRunAsService userRunAsService;
    @Autowired
    private UserService userService;

    /**
     * @Author haien
     * @Description 判断当前用户是否为代理老板，并展示其可代理|委托的列表
     * @Date 2019/4/10
     * @Param [loginUser, model]
     * @return java.lang.String
     **/
    @RequestMapping
    public String runasList(@CurrentUser User loginUser, Model model) {
        //当前用户能代理的用户id
        model.addAttribute("fromUserIds",
                userRunAsService.findFromUserIds(loginUser.getId()));
        //当前用户能委托的用户id
        model.addAttribute("toUserIds",
                userRunAsService.findToUserIds(loginUser.getId()));

        List<User> allUsers = userService.findAll();
        allUsers.remove(loginUser);
        model.addAttribute("allUsers", allUsers);

        Subject subject = SecurityUtils.getSubject();
        model.addAttribute("isRunas", subject.isRunAs());
        //loginUser是老板，但是个代理老板
        if(subject.isRunAs()) {
            //获取之前的身份信息，一个用户可以切换很多身份，之前的身份使用栈存储
            String previousUsername =
                    (String)subject.getPreviousPrincipals().getPrimaryPrincipal(); //得到最近一个
            model.addAttribute("previousUsername", previousUsername);
        }

        return "runas";
    }

    /**
     * @Author haien
     * @Description 将当前用户操作权授予指定用户，则该指定用户下次可切换到当前用户
     * @Date 2019/4/10
     * @Param [loginUser, toUserId, redirectAttributes]
     * @return java.lang.String
     **/
    @RequestMapping("/grant/{toUserId}")
    public String grant(@CurrentUser User loginUser, @PathVariable("toUserId") Long toUserId,
            RedirectAttributes redirectAttributes) {

        //判断要切换的身份是否为自己
        if(loginUser.getId().equals(toUserId)) {
            redirectAttributes.addFlashAttribute("msg",
                    "自己不能切换到自己的身份");
            return "redirect:/runas";
        }

        //不是则新增委托-代理关系到数据库
        userRunAsService.grantRunAs(loginUser.getId(), toUserId);
        redirectAttributes.addFlashAttribute("msg", "操作成功");
        return "redirect:/runas";
    }


    /**
     * @Author haien
     * @Description 把当前用户的操作权从指定用户手中回收
     * @Date 2019/4/10
     * @Param [loginUser, toUserId, redirectAttributes]
     * @return java.lang.String
     **/
    @RequestMapping("/revoke/{toUserId}")
    public String revoke(@CurrentUser User loginUser, @PathVariable("toUserId") Long toUserId,
            RedirectAttributes redirectAttributes) {
        userRunAsService.revokeRunAs(loginUser.getId(), toUserId);
        redirectAttributes.addFlashAttribute("msg", "操作成功");
        return "redirect:/runas";
    }

    @RequestMapping("/switchTo/{switchToUserId}")
    public String switchTo(@CurrentUser User loginUser,
                           @PathVariable("switchToUserId") Long switchToUserId,
                           RedirectAttributes redirectAttributes) {

        Subject subject = SecurityUtils.getSubject();

        //查找目标用户的身份
        User switchToUser = userService.findOne(switchToUserId);

        //判断能否切换
        if(loginUser.equals(switchToUser)) {
            redirectAttributes.addFlashAttribute("msg",
                    "自己不能切换到自己的身份");
            return "redirect:/runas";
        }
        if(switchToUser == null || !userRunAsService.exists(switchToUserId, loginUser.getId())) {
            redirectAttributes.addFlashAttribute("msg",
                    "对方没有授予您身份，不能切换");
            return "redirect:/runas";
        }

        //切换
        subject.runAs(new SimplePrincipalCollection(switchToUser.getUsername(), ""));

        redirectAttributes.addFlashAttribute("msg", "操作成功");
        redirectAttributes.addFlashAttribute("needRefresh", "true");
        return "redirect:/runas";
    }

    /**
     * @Author haien
     * @Description 切换回上一个身份
     * @Date 2019/4/10
     * @Param [redirectAttributes]
     * @return java.lang.String
     **/
    @RequestMapping("/switchBack")
    public String switchBack(RedirectAttributes redirectAttributes) {

        Subject subject = SecurityUtils.getSubject();
        if(subject.isRunAs()) {
            //切换回上一个身份；如果A切换到B，B又切换到C，那么需要执行两次才能回到A
            subject.releaseRunAs();
        }

        redirectAttributes.addFlashAttribute("msg", "操作成功");
        redirectAttributes.addFlashAttribute("needRefresh", "true");
        return "redirect:/runas";
    }

}