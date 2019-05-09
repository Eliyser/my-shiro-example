package com.haien.chapter23.client;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;

/**
 * @Author haien
 * @Description 自定义ShiroFilter的工厂bean，主要是为了能用字串动态定义不同应用的不同拦截器（链）
 * @Date 2019/5/7
 **/
public class ClientShiroFilterFactoryBean extends ShiroFilterFactoryBean
        implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        //this.applicationContext飘红的原因是没有把spring-beans依赖add to classpath
        this.applicationContext=applicationContext;
    }

    /**
     * @Author haien
     * @Description 设置拦截器，加入拦截器集合；格式：filterName=1;filterName=2
     * @Date 2019/4/28
     * @Param [filters]
     * @return void
     **/
    public void setFiltersStr(String filters) { //多个用分号分隔,filterName=1;filterName=2
        if(StringUtils.isEmpty(filters)) return;

        String[] filterArray = filters.split(";");
        for(String filter : filterArray) {
            String[] o = filter.split("=");
            //根据名字获取过滤器实例并存入过滤器集合中
            getFilters().put(o[0], (Filter)applicationContext.getBean(o[1]));
        }
    }

    /**
     * @Author haien
     * @Description 设置拦截器链，即url=filterName1[config],filterName2，加入拦截器链集合
     * @Date 2019/4/28
     * @Param [filterChainDefinitions
     *         格式：url=filterName1[config],filterName2;url=filterName1[config],filterName2]
     * @return void
     **/
    public void setFilterChainDefinitionsStr(String filterChainDefinitions) {

        if(StringUtils.isEmpty(filterChainDefinitions)) return;

        String[] chainDefinitionsArray = filterChainDefinitions.split(";");
        for(String filter : chainDefinitionsArray) {
            String[] o = filter.split("=");
            //将url和对应过滤器加入拦截器链集合
            getFilterChainDefinitionMap().put(o[0], o[1]);
        }
    }
}





































