package com.haien.chapter18.service.impl;

import com.haien.chapter18.dao.ResourceDao;
import com.haien.chapter18.entity.Resource;
import com.haien.chapter18.service.ResourceService;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("ResourceService")
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    @Override
    public Resource createResource(Resource resource) {
        return resourceDao.createResource(resource);
    }

    @Override
    public Resource updateResource(Resource resource) {
        return resourceDao.updateResource(resource);
    }

    @Override
    public void deleteResource(Long resourceId) {
        resourceDao.deleteResource(resourceId);
    }

    @Override
    public Resource findOne(Long resourceId) {
        return resourceDao.findOne(resourceId);
    }

    @Override
    public List<Resource> findAll() {
        return resourceDao.findAll();
    }

    /**
     * @Author haien
     * @Description 查询资源对应的权限字符串
     * @Date 2019/3/13
     * @Param [resourceIds]
     * @return java.util.Set<java.lang.String>
     **/
    @Override
    public Set<String> findPermissions(Set<Long> resourceIds) {
        Set<String> permissions = new HashSet<String>();
        for(Long resourceId : resourceIds) {
            Resource resource = findOne(resourceId);
            if(resource != null && !StringUtils.isEmpty(resource.getPermission())) {
                permissions.add(resource.getPermission());
            }
        }
        return permissions;
    }

    /**
     * @Author haien
     * @Description 根据用户权限字符串查找资源集合
     * @Date 2019/3/13
     * @Param [permissions]
     * @return java.util.List<com.haien.chapter16.entity.Resource>
     **/
    @Override
    public List<Resource> findMenus(Set<String> permissions) {
        //查询全部资源
        List<Resource> allResources = findAll();
        List<Resource> menus = new ArrayList<Resource>();
        for(Resource resource : allResources) {
            //根菜单不处理
            if(resource.isRootNode()) {
                continue;
            }
            //非菜单不处理
            if(resource.getType() != Resource.ResourceType.menu) {
                continue;
            }
            //没有相应权限不处理
            if(!hasPermission(permissions, resource)) {
                continue;
            }
            menus.add(resource);
        }
        return menus;
    }
    /**
     * @Author haien
     * @Description 判断资源用户是否能访问资源，即用户权限应大于等于资源要求权限
     * @Date 2019/3/13
     * @Param [permissions, resource]
     * @return boolean
     **/
    private boolean hasPermission(Set<String> permissions, Resource resource) {
        //为空表示不需要权限
        if(StringUtils.isEmpty(resource.getPermission())) {
            return true;
        }

        for(String permission : permissions) {
            WildcardPermission p1 = new WildcardPermission(permission);
            WildcardPermission p2 = new WildcardPermission(resource.getPermission());
            /*p1.implies(p2)：判断p1是否包含（或等于）p2的权限，
            如p1为user:*或user，p2为user:create，返回true*/
            if(p1.implies(p2)) {
                return true;
            }
        }
        return false;
    }
}
