package com.haien.chapter19.service.impl;

import com.haien.chapter19.dao.UrlFilterDao;
import com.haien.chapter19.entity.UrlFilter;
import com.haien.chapter19.service.UrlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service("urlFilterService")
public class UrlFilterServiceImpl implements UrlFilterService {

    @Autowired
    private UrlFilterDao urlFilterDao;

    @Autowired
    private ShiroFilterChainManager shiroFilerChainManager;

    @Override
    public UrlFilter createUrlFilter(UrlFilter urlFilter) {
        urlFilterDao.createUrlFilter(urlFilter);
        initFilterChain(); //重新初始化拦截器链
        return urlFilter;
    }

    @Override
    public UrlFilter updateUrlFilter(UrlFilter urlFilter) {
        urlFilterDao.updateUrlFilter(urlFilter);
        initFilterChain();
        return urlFilter;
    }

    @Override
    public void deleteUrlFilter(Long urlFilterId) {
        urlFilterDao.deleteUrlFilter(urlFilterId);
        initFilterChain();
    }

    @Override
    public UrlFilter findOne(Long urlFilterId) {
        return urlFilterDao.findOne(urlFilterId);
    }

    @Override
    public List<UrlFilter> findAll() {
        return urlFilterDao.findAll();
    }

    /**
     * @Author haien
     * @Description 初始化Shiro的url拦截器，用于在url表改动后，将数据库发送的改动同步到Shiro中。
     * @Date 2019/4/5
     * @Param []
     * @return void
     **/
    @PostConstruct //当DI容器实例化当前bean时，该方法会自动执行
    public void initFilterChain() {
        //将配置文件和数据库的拦截器链合并
        shiroFilerChainManager.initFilterChains(findAll());
    }
}
