package com.haien.chapter17.web.controller;

import com.haien.chapter17.entity.Client;
import com.haien.chapter17.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * @Author haien
     * @Description 查看所有客户端
     * @Date 2019/3/24
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("clientList", clientService.findAll());
        return "client/list";
    }

    /**
     * @Author haien
     * @Description 映射新增客户端页面
     * @Date 2019/3/24
     * @Param [model]
     * @return java.lang.String
     **/
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("op", "新增");
        return "client/edit";
    }

    /**
     * @Author haien
     * @Description 处理新增客户端请求
     * @Date 2019/3/24
     * @Param [client, redirectAttributes]
     * @return java.lang.String
     **/
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Client client, RedirectAttributes redirectAttributes) {
        clientService.createClient(client);
        redirectAttributes.addFlashAttribute("msg", "新增成功");
        return "redirect:/client";
    }

    /**
     * @Author haien
     * @Description 查询某个客户端的信息，映射修改客户端页面
     * @Date 2019/3/24
     * @Param [id, model]
     * @return java.lang.String
     **/
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("client", clientService.findOne(id));
        model.addAttribute("op", "修改");
        return "client/edit";
    }

    /**
     * @Author haien
     * @Description 处理修改客户端请求
     * @Date 2019/3/24
     * @Param [client, redirectAttributes]
     * @return java.lang.String
     **/
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String update(Client client, RedirectAttributes redirectAttributes) {
        clientService.updateClient(client);
        redirectAttributes.addFlashAttribute("msg", "修改成功");
        return "redirect:/client";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String showDeleteForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("client", clientService.findOne(id));
        model.addAttribute("op", "删除");
        return "client/edit";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(id);
        redirectAttributes.addFlashAttribute("msg", "删除成功");
        return "redirect:/client";
    }

}
