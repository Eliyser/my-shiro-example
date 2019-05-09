package com.haien.chapter17.service.impl;

import com.haien.chapter17.service.ClientService;
import com.haien.chapter17.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @Author haien
 * @Description 在缓存中对授权码code和令牌token进行维护
 * @Date 2019/3/20
 **/
@Service("OAuthService")
public class OAuthServiceImpl implements OAuthService {

    //spring的cache接口
    private Cache cache;

    @Autowired
    private ClientService clientService;

    /**
     * @Author haien
     * @Description OAuthServiceImpl的构造器，注入CacheManager
     * @Date 14:10 2019/3/24
     * @Param [cacheManager]
     * @return
     **/
    @Autowired
    public OAuthServiceImpl(CacheManager cacheManager) { //spring的CacheManager，等待从外面注入
        this.cache = cacheManager.getCache("code-cache"); //获取名为code-cache的cache
    }

    /**
     * @Author haien
     * @Description 添加缓存条目：key=authCode.value=username
     * @Date 2019/3/24
     * @Param [authCode, username]
     * @return void
     **/
    @Override
    public void addAuthCode(String authCode, String username) {
        cache.put(authCode, username);
    }

    /**
     * @Author haien
     * @Description 添加缓存条目：key=accessToken,value=username
     * @Date 2019/3/24
     * @Param [accessToken, username]
     * @return void
     **/
    @Override
    public void addAccessToken(String accessToken, String username) {
        cache.put(accessToken, username);
    }

    /**
     * @Author haien
     * @Description 通过authCode获取cache中的username
     * @Date 2019/3/24
     * @Param [authCode]
     * @return java.lang.String
     **/
    @Override
    public String getUsernameByAuthCode(String authCode) {
        return (String)cache.get(authCode).get();
    }

    /**
     * @Author haien
     * @Description 通过AccessToken获取cache中的username
     * @Date 2019/3/24
     * @Param [accessToken]
     * @return java.lang.String
     **/
    @Override
    public String getUsernameByAccessToken(String accessToken) {
        return (String)cache.get(accessToken).get();
    }

    /**
     * @Author haien
     * @Description 判断cache中是否有authCode这个key
     * @Date 2019/3/24
     * @Param [authCode]
     * @return boolean
     **/
    @Override
    public boolean checkAuthCode(String authCode) {
        return cache.get(authCode) != null;
    }

    /**
     * @Author haien
     * @Description 判断cache中是否有accessToken这个key
     * @Date 2019/3/24
     * @Param [accessToken]
     * @return boolean
     **/
    @Override
    public boolean checkAccessToken(String accessToken) {
        return cache.get(accessToken) != null;
    }

    /**
     * @Author haien
     * @Description 判断数据库是否有id为clientid的客户端
     * @Date 2019/3/24
     * @Param [clientId]
     * @return boolean
     **/
    @Override
    public boolean checkClientId(String clientId) {
        return clientService.findByClientId(clientId) != null;
    }

    /**
     * @Author haien
     * @Description 判断数据库是否有密钥为clientSecret的客户端
     * @Date 2019/3/24
     * @Param [clientSecret]
     * @return boolean
     **/
    @Override
    public boolean checkClientSecret(String clientSecret) {
        return clientService.findByClientSecret(clientSecret) != null;
    }

    /**
     * @Author haien
     * @Description 获取authCode和AccessToken过期时间
     * @Date 2019/3/24
     * @Param []
     * @return long
     **/
    @Override
    public long getExpireIn() {
        return 3600L;
    }
}
