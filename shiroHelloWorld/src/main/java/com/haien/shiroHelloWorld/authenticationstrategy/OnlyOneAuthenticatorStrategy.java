package com.haien.shiroHelloWorld.authenticationstrategy;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

/**
 * @Author haien
 * @Description 自定义验证策略
 * @Date 2019/2/17
 **/
public class OnlyOneAuthenticatorStrategy extends AbstractAuthenticationStrategy {
    @Override
    public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms,
           AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo();
    }

    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token,
           AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }

    /**
     * @Author haien
     * @Description 真正重写的方法
     * @Date 2019/2/17
     * @Param [realm, token, singleRealmInfo, aggregateInfo, t]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token,
           AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t)
            throws AuthenticationException {

        AuthenticationInfo info;
        if(singleRealmInfo == null){ //若当前身份凭证为空，说明未通过认证
            info=aggregateInfo; //返回之前的身份凭证集合
        }else{ //否则需要加入集合
            if(aggregateInfo==null){ //若集合为空，则返回当前的身份凭证
                info=singleRealmInfo;
            }else{ //两个都不为空才需要加入集合
                info=merge(singleRealmInfo,aggregateInfo);
                if(info.getPrincipals().getRealmNames().size()>1){
                    System.out.println(info.getPrincipals().getRealmNames()); //打印集合
                    throw new AuthenticationException("Authentication token of type ["
                      +token.getClass()+"]"+"could not be authenticated by any configured " +
                            "realms. Please ensure that only one realm can authenticated " +
                            "these tokens");
                }
            }
        }

        return info;
    }

    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token,
           AuthenticationInfo aggregate) throws AuthenticationException {
        return aggregate;
    }
}
