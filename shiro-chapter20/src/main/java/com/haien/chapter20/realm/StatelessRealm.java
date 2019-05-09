package com.haien.chapter20.realm;

import com.haien.chapter20.codec.HmacSHA256Utils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class StatelessRealm extends AuthorizingRealm {
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username=(String)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.addRole("admin");
        return authorizationInfo;
    }

    /**
     * @Author haien
     * @Description 服务端利用用户名和请求参数生成消息摘要，与客户端消息摘要匹配，以此作为身份验证；
     *              缺点是一旦别人截获请求，则可以重复请求。
     * @Date 2019/4/8
     * @Param [token]
     * @return org.apache.shiro.authc.AuthenticationInfo
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        StatelessToken statelessToken=(StatelessToken)token;
        String username=statelessToken.getUsername();
        //根据用户名获取密钥（和客户端一样）
        String key=getKey(username);
        //在服务器端使用对客户端参数生成消息摘要
        String serverDigest=HmacSHA256Utils.digest(key,statelessToken.getParams());
        //然后进行客户端消息摘要和服务端消息摘要的匹配
        return new SimpleAuthenticationInfo(username,serverDigest,getName());
    }

    /**
     * @Author haien
     * @Description 获取服务端密钥，此处硬编码一个（原本应该是有一套和客户端一致的算法的）
     * @Date 2019/4/8
     * @Param [username]
     * @return java.lang.String
     **/
    private String getKey(String username){
        if("admin".equals(username))
            return "dadadswdewq2ewdwqdwadsadasd";

        return null;
    }
}
