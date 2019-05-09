package com.haien.chapter16.web.taglib;

import com.haien.chapter16.entity.Organization;
import com.haien.chapter16.entity.Resource;
import com.haien.chapter16.entity.Role;
import com.haien.chapter16.service.OrganizationService;
import com.haien.chapter16.service.ResourceService;
import com.haien.chapter16.service.RoleService;
import com.haien.spring.SpringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @Author haien
 * @Description zhang-funcitons.tld调用此类中的方法包装成标签提供给jsp使用
 * @Date 2019/3/18
 **/
public class Functions {
    /**
     * @Author haien
     * @Description 判断集合中是否含有某个元素
     * @Date 2019/3/14
     * @Param [iterable, element]
     * @return boolean
     **/
    public static boolean in(Iterable iterable,Object element){
        if(iterable==null) return false;

        return CollectionUtils.contains(iterable.iterator(),element);
    }

    /**
     * @Author haien
     * @Description 根据机构id获取机构名称
     * @Date 2019/3/18
     * @Param [organizationId]
     * @return java.lang.String
     **/
    public static String organizationName(Long organizationId) {
        Organization organization = getOrganizationService().findOne(organizationId);
        if(organization == null) {
            return "";
        }
        return organization.getName();
    }

    /**
     * @Author haien
     * @Description 根据机构id集合获取机构名称，用逗号连接成一个字符串返回
     * @Date 2019/3/18
     * @Param [organizationIds]
     * @return java.lang.String
     **/
    public static String organizationNames(Collection<Long> organizationIds) {
        //机构id为空则返回空字符串
        if(CollectionUtils.isEmpty(organizationIds)) {
            return "";
        }

        //开始连接名称
        StringBuilder s = new StringBuilder();
        for(Long organizationId : organizationIds) {
            Organization organization = getOrganizationService().findOne(organizationId);
            if(organization == null) {
                return "";
            }
            s.append(organization.getName());
            s.append(",");
        }

        //把最后一个逗号去掉
        if(s.length() > 0) {
            s.deleteCharAt(s.length() - 1);
        }

        return s.toString();
    }

    /**
     * @Author haien
     * @Description 根据角色id获取角色的中文名称
     * @Date 2019/3/18
     * @Param [roleId]
     * @return java.lang.String
     **/
    public static String roleName(Long roleId) {
        Role role = getRoleService().findOne(roleId);
        if(role == null) {
            return "";
        }
        return role.getDescription();
    }

    /**
     * @Author haien
     * @Description 根据角色的id集合获取角色的中文名称，用逗号连接成字符串返回
     * @Date 2019/3/18
     * @Param [roleIds]
     * @return java.lang.String
     **/
    public static String roleNames(Collection<Long> roleIds) {
        //id集合为空则返回空字符串
        if(CollectionUtils.isEmpty(roleIds)) {
            return "";
        }

        //开始用逗号连接
        StringBuilder s = new StringBuilder();
        for(Long roleId : roleIds) {
            Role role = getRoleService().findOne(roleId);
            if(role == null) {
                return "";
            }
            s.append(role.getDescription());
            s.append(",");
        }

        //删掉最后一个逗号
        if(s.length() > 0) {
            s.deleteCharAt(s.length() - 1);
        }

        return s.toString();
    }

    /**
     * @Author haien
     * @Description 根据资源id获取资源名称
     * @Date 2019/3/18
     * @Param [resourceId]
     * @return java.lang.String
     **/
    public static String resourceName(Long resourceId) {
        Resource resource = getResourceService().findOne(resourceId);
        if(resource == null) {
            return "";
        }
        return resource.getName();
    }

    /**
     * @Author haien
     * @Description 根据资源id集合获取资源名称集合
     * @Date 2019/3/18
     * @Param [resourceIds]
     * @return java.lang.String
     **/
    public static String resourceNames(Collection<Long> resourceIds) {
        if(CollectionUtils.isEmpty(resourceIds)) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        for(Long resourceId : resourceIds) {
            Resource resource = getResourceService().findOne(resourceId);
            if(resource == null) {
                return "";
            }
            s.append(resource.getName());
            s.append(",");
        }

        if(s.length() > 0) {
            s.deleteCharAt(s.length() - 1);
        }

        return s.toString();
    }

    private static OrganizationService organizationService;
    private static RoleService roleService;
    private static ResourceService resourceService;

    /**
     * @Author haien
     * @Description 从Spring容器获取bean注入
     * @Date 2019/3/18
     * @Param []
     * @return com.haien.chapter16.service.OrganizationService
     **/
    public static OrganizationService getOrganizationService() {
        if(organizationService == null) {
            organizationService = SpringUtils.getBean(OrganizationService.class);
        }
        return organizationService;
    }
    public static RoleService getRoleService() {
        if(roleService == null) {
            roleService = SpringUtils.getBean(RoleService.class);
        }
        return roleService;
    }
    public static ResourceService getResourceService() {
        if(resourceService == null) {
            resourceService = SpringUtils.getBean(ResourceService.class);
        }
        return resourceService;
    }
}
