package com.haien.chapter19.service.impl;

import org.apache.shiro.config.Ini;
import org.apache.shiro.util.Nameable;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author haien
 * @Description 自定义拦截器链管理器，将数据库中的拦截器链加载进来
 * @Date 2019/4/6
 **/
public class CustomDefaultFilterChainManager extends DefaultFilterChainManager {
    //配置文件中all拦截器链，即默认的静态拦截器链，数据库中为动态的
    private Map<String,String> filterChainDefinitionMap=null;
    //登录地址
    private String loginUrl;
    //成功地址
    private String successUrl;
    private String unauthorizedUrl;

    public CustomDefaultFilterChainManager() {
        //调用父类方法，注册默认拦截器
        setFilters(new LinkedHashMap<String, Filter>());
        //注册默认拦截器链
        setFilterChains(new LinkedHashMap<String, NamedFilterList>());
        addDefaultFilters(true);
    }

    /**
     * @Author haien
     * @Description 注册自定义的拦截器，如ShiroFilterFactoryBean中的filters属性
     * @Date 2019/4/6
     * @Param [customFilters]
     * @return void
     **/
    public void setCustomFilters(Map<String,Filter> customFilters){
        for (Map.Entry<String,Filter> entry:customFilters.entrySet())
            addFilter(entry.getKey(),entry.getValue(),false);
    }

    /**
     * @Author haien
     * @Description 解析配置文件中all拦截器链
     * @Date 2019/4/6
     * @Param [definitions]
     * @return void
     **/
    public void setDefaultFilterChainDefinitions(String definitions){
        Ini ini=new Ini();
        ini.load(definitions);
        Ini.Section section=ini.getSection(IniFilterChainResolverFactory.URLS);

        if(CollectionUtils.isEmpty(section))
            section=ini.getSection(Ini.DEFAULT_SECTION_NAME);

        //把解析得到的默认拦截器链注册到本类属性中
        setFilterChainDefinitionMap(section);
    }

    @PostConstruct
    public void init(){
        //给拦截器配置上属性，这里是三个url
        Map<String,Filter> filters=getFilters();
        if(!CollectionUtils.isEmpty(filters)){
            for (Map.Entry<String,Filter> entry:filters.entrySet()){
                String name=entry.getKey();
                Filter filter=entry.getValue();
                applyGlobalPropertiesIfNecessary(filter); //配置三个url
                if(filter instanceof Nameable)
                    ((Nameable)filter).setName(name);
            }
        }

        //将拦截器构建成拦截器链
        Map<String,String> chains=getFilterChainDefinitionMap();
        if(!CollectionUtils.isEmpty(chains)){
            for(Map.Entry<String,String> entry:chains.entrySet()){
                String url=entry.getKey();
                String chainDefinition=entry.getValue();
                createChain(url,chainDefinition);
            }
        }
    }

    /**
     * @Author haien
     * @Description 忽略，交给Spring管理，因此Filter的相关配置会在Spring配置中完成
     * @Date 2019/4/7
     * @Param [filter]
     * @return void
     **/
    @Override
    protected void initFilter(Filter filter) {
        //ignore
    }

    /**
     * @Author haien
     * @Description 组合多个拦截器链为一个新的FilterChain代理
     * @Date 2019/4/7
     * @Param [original, chainNames]
     * @return javax.servlet.FilterChain
     **/
    public FilterChain proxy(FilterChain original, List<String> chainNames){
        NamedFilterList configued=new SimpleNamedFilterList(chainNames.toString());
        for(String chainName:chainNames)
            configued.addAll(getChain(chainName));
        return configued.proxy(original);
    }

    /**
     * @Author haien
     * @Description 配置三个url
     * @Date 2019/4/7
     * @Param [filter]
     * @return void
     **/
    private void applyGlobalPropertiesIfNecessary(Filter filter){
        applyLoginUrlIfNecessary(filter);
        applySuccessUrlIfNecessary(filter);
        applyUnauthorizedUrlIdNecessary(filter);
    }
    private void applyLoginUrlIfNecessary(Filter filter){
        String loginUrl=getLoginUrl();
        //判空、filter是否为AccessControllerFilter，因为只有此类才有LoginUrl属性
        if(StringUtils.hasText(loginUrl) && (filter instanceof AccessControlFilter)){
            AccessControlFilter acFilter=(AccessControlFilter)filter;
            //判断该Filter是否已配置loginUrl
            String existingLoginUrl=acFilter.getLoginUrl();
            if(AccessControlFilter.DEFAULT_LOGIN_URL.equals(existingLoginUrl)) //是默认的即未配置
                acFilter.setLoginUrl(loginUrl);
        }
    }
    private void applySuccessUrlIfNecessary(Filter filter){
        String seccessUrl=getSuccessUrl();
        //判断是否AuthenticationFilter，因为只有此类才有successUrl属性
        if(StringUtils.hasText(successUrl) && (filter instanceof AuthenticationFilter)){
            AuthenticationFilter authcFilter=(AuthenticationFilter)filter;
            String existingSuccessUrl=authcFilter.getSuccessUrl();
            if(AuthenticationFilter.DEFAULT_SUCCESS_URL.equals(existingSuccessUrl))
                authcFilter.setSuccessUrl(successUrl);
        }
    }
    private void applyUnauthorizedUrlIdNecessary(Filter filter){
        String unauthorizedUrl=getUnauthorizedUrl();
        if(StringUtils.hasText(unauthorizedUrl) && (filter instanceof AuthorizationFilter)){
            AuthorizationFilter authzFilter=(AuthorizationFilter)filter;
            String existingUnauthorizedUrl=authzFilter.getUnauthorizedUrl();
            if(existingUnauthorizedUrl==null)
                authzFilter.setUnauthorizedUrl(unauthorizedUrl);
        }
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }
    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }
    public String getLoginUrl() {
        return loginUrl;
    }
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    public String getSuccessUrl() {
        return successUrl;
    }
    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }
    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }
    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }
}





























