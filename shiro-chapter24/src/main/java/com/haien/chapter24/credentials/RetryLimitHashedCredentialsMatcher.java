package com.haien.chapter24.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    private Cache<String,AtomicInteger> passwordRetryCache;

    /**
  15:33@Author haien
     * @Description
     * @Date 2019/3/4
     * @Param
     * @return
     **/
    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        /* 自己创建cache
        public RetryLimitHashedCredentialsMatcher() {
        CacheManager cacheManager=CacheManager.create(
                CacheManager.class.getClassLoader()
                        .getResource("config/password-ehcache.xml"));
        */
        passwordRetryCache=cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获取表单参数
        String username=(String)token.getPrincipal();

        /* 不用获取缓存对象，它一定存在
        //获取该用户的缓存对象
        Element element=passwordRetryCache.get(username);
        //缓存对象不存在说明上一次登录成功或从未登录过
        if(element==null){
            element=new Element(username,new AtomicInteger(0)); //new一个并置零
            passwordRetryCache.put(element);
        }
        //从缓存对象中获取重试次数
        AtomicInteger retryCount=(AtomicInteger)element.getObjectValue();
        */
        //直接获取重试次数
        AtomicInteger retryCount=passwordRetryCache.get(username);
        if(retryCount==null){
            retryCount=new AtomicInteger(0);
            passwordRetryCache.put(username,retryCount);
        }

        //第6次起无论输入对错都禁止
        if(retryCount.incrementAndGet()>5) //+1
            throw new ExcessiveAttemptsException();

        boolean matches=super.doCredentialsMatch(token,info);
        if(matches)
            passwordRetryCache.remove(username);
        return matches;
    }
}
