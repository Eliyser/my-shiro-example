package com.haien.shiroHelloWorld.authenticationstrategy;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.CollectionUtils;

import java.util.Collection;

/**
 * @Author haien
 * @Description 自定义验证策略
 * @Date 2019/2/17
 **/
public class AtLeastTwoAuthenticatorStrategy extends AbstractAuthenticationStrategy {
    /**
     * @Author haien
     * @Description 在所有Realm验证之前调用
     * @Date 2019/2/17
     * @Param [realms, token]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms,
           AuthenticationToken token) throws AuthenticationException {
        //和父类方法一样，返回一个空的身份凭证
        return new SimpleAuthenticationInfo();
    }

    /**
     * @Author haien
     * @Description 在每个Realm验证之前调用
     * @Date 2019/2/17
     * @Param [realm, token, aggregate]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token,
           AuthenticationInfo aggregate) throws AuthenticationException {
        //和父类方法一样，返回之前合并的
        return aggregate;
    }

    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token,
           AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t)
           throws AuthenticationException {
        //也是和父类方法一样
        AuthenticationInfo info;
        if(singleRealmInfo==null){
            info=aggregateInfo;
        }else {
            if(aggregateInfo==null){
                info=singleRealmInfo;
            }else{
                //调用父类merge()将所有身份凭证集合起来，最后一起返回
                info=merge(singleRealmInfo,aggregateInfo);
            }
        }
        return info;
    }

    /**
     * @Author haien
     * @Description 真正重写的方法：判断是否至少两个Realm通过
     * @Date 2019/2/17
     * @Param [token, aggregate]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token,
           AuthenticationInfo aggregate) throws AuthenticationException {

        if(aggregate==null||CollectionUtils.isEmpty(aggregate.getPrincipals())
                ||aggregate.getPrincipals().getRealmNames().size()<2){ //判断是否至少有两个通过
            throw new AuthenticationException("Authentication token of type ["
                    +token.getClass()+"]"+"could not be authenticated by any configured realms." +
                    "please ensure that at least two realm can authenticate these tokens.");
        }

        //这一步即父类方法体
        return aggregate;
    }
}
