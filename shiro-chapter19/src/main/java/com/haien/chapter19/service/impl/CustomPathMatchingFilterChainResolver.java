package com.haien.chapter19.service.impl;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author haien
 * @Description 自定义拦截器链解析器，不要只解析一条拦截器链就返回
 * @Date 2019/4/7
 **/
public class CustomPathMatchingFilterChainResolver
        extends PathMatchingFilterChainResolver {
    private CustomDefaultFilterChainManager customDefaultFilterChainManager;

    public void setCustomDefaultFilterChainManager(
            CustomDefaultFilterChainManager customDefaultFilterChainManager){
        this.customDefaultFilterChainManager=customDefaultFilterChainManager;
        setFilterChainManager(customDefaultFilterChainManager);
    }

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response,
                                FilterChain originalChain){
        FilterChainManager filterChainManager=getFilterChainManager();
        if(!filterChainManager.hasChains())
            return null;

        String requestURI=getPathWithinApplication(request);
        List<String> chainNames=new ArrayList<>(); //存放匹配的多条拦截器链
        //遍历all拦截器链，匹配当前url则放入集合
        for(String pathPattern:filterChainManager.getChainNames()){
            if(pathMatches(pathPattern,requestURI))
                chainNames.add(pathPattern);
        }

        if(chainNames.size()==0)
            return null;

        return customDefaultFilterChainManager.proxy(originalChain,chainNames);
    }
}



































