package com.haien.chapter19.service.impl;

import com.haien.chapter19.entity.UrlFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShiroFilterChainManager {
    //过滤链管理器，管理所有过滤器，包含增删查改等操作
    @Resource
    private DefaultFilterChainManager filterChainManager; //注入的是CustomDefaultFilterChainManager
    //默认过滤链，包含在配置文件中提到的所有过滤器
    private Map<String,NamedFilterList> defaultFilterChains;

    /**
     * @Author haien
     * @Description 获取配置文件中all过滤器，之后会自动与数据库中包含的过滤器合并
     * @Date 2019/4/5
     * @Param []
     * @return void
     **/
    @PostConstruct //DI容器实例化该bean时自动执行该方法
    public void inti(){
        //获取配置文件中all拦截器（静态的，不会变）
        defaultFilterChains=new HashMap<>(filterChainManager.getFilterChains());
    }

    /**
     * @Author haien
     * @Description 初始化过滤链，默认+数据库
     * @Date 2019/4/5
     * @Param [urlFilters]
     * @return void
     **/
    public void initFilterChains(List<UrlFilter> urlFilters){
        //1. 删除以前老的filter chain并注册默认的
        filterChainManager.getFilterChains().clear();
        if(defaultFilterChains!=null)
            filterChainManager.getFilterChains().putAll(defaultFilterChains);

        //2. 注册数据库中的
        for(UrlFilter urlFilter:urlFilters){
            String url=urlFilter.getUrl();
            //注册roles-filter关系的过滤器
            if(!StringUtils.isEmpty(urlFilter.getRoles()))
                filterChainManager.addToChain(url,"roles",urlFilter.getRoles());

            //注册perms-filter关系的拦截器
            if(!StringUtils.isEmpty(urlFilter.getPermissions()))
                filterChainManager.addToChain(url,"perms",urlFilter.getPermissions());
        }
    }
}

































