package com.haien.shiroChapter11.credentials;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author haien
 * @Description 自定义CredentialsMatcher实现类，限制密码重试次数
 *              输入正确清除cache中的记录，否则cache中的重试次数+1，如果超出5次那么抛出异常
 * @Date 2019/2/24
 **/
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    //密码重试次数缓存对象集合
    private Ehcache passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher() {
        CacheManager cacheManager=CacheManager.create(
                CacheManager.class.getClassLoader()
                        .getResource("config/password-ehcache.xml"));
        passwordRetryCache=cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获取表单参数
        String username=(String)token.getPrincipal();
        //获取该用户的缓存对象
        Element element=passwordRetryCache.get(username);
        //缓存对象不存在说明上一次登录成功或从未登录过
        if(element==null){
            element=new Element(username,new AtomicInteger(0)); //new一个并置零
            passwordRetryCache.put(element);
        }

        //从缓存对象中获取重试次数
        AtomicInteger retryCount=(AtomicInteger)element.getObjectValue();
        //第6次起无论输入对错都禁止
        if(retryCount.incrementAndGet()>5) //+1
            throw new ExcessiveAttemptsException();

        boolean matches=super.doCredentialsMatch(token,info);
        if(matches)
            passwordRetryCache.remove(username);
        return matches;
    }
}

























































