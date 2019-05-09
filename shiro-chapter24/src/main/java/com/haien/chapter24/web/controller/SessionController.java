package com.haien.chapter24.web.controller;

import com.haien.chapter24.Constants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @Author haien
 * @Description 会话控制器
 * @Date 2019/5/9
 **/
@Controller
@RequestMapping("/sessions")
@RequiresPermissions("session:*")
public class SessionController {
    @Resource
    private SessionDAO sessionDAO;

    /**
     * @Author haien
     * @Description 获取all会话
     * @Date 2019/5/9
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping()
    public String list(Model model){
        //获取all活跃会话的集合，但如果在线用户非常多，则应采取分页获取
        Collection<Session> sessions=sessionDAO.getActiveSessions();

        model.addAttribute("sessions",sessions);
        model.addAttribute("sessionCount",sessions.size());

        return "sessions/list";
    }

    /**
     * @Author haien
     * @Description 强制退出指定会话，退出方法即给该会话加一标志属性，
     *              之后通过ForceLogoutFilter强制退出
     * @Date 2019/5/9
     * @Param [sessionId, redirectAttributes]
     * @return java.lang.String
     **/
    @RequestMapping("/{sessionId}/forceLogout")
    public String forceLogout(@PathVariable("sessionId") String sessionId,
                              RedirectAttributes redirectAttributes){
        try {
            //根据id获取会话
            Session session = sessionDAO.readSession(sessionId);
            if (session != null)
                session.setAttribute(Constants.SESSION_FORCE_LOGOUT_KEY, Boolean.TRUE);
        } catch (Exception e){
            //ignore
        }

        redirectAttributes.addFlashAttribute("msg","强制退出成功!");
        return "redirect:sessions";
    }
}


















































