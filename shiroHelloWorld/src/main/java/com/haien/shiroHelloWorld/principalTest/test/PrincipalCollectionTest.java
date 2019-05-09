package com.haien.shiroHelloWorld.principalTest.test;

import com.haien.shiroHelloWorld.chapter6.entity.User;
import com.haien.shiroHelloWorld.test.BaseTest;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class PrincipalCollectionTest extends BaseTest {
    @Test
    public void testPrincipalCollection(){
        login("classpath:config/shiro-multirealm.ini",
                "zhang","123");

        Subject subject=subject();

        //获取Map中第一个Principal，即PrimaryPrincipal
        Object primaryPrincipal1=subject.getPrincipal();
        //获取PrincipalCollection
        PrincipalCollection principalCollection=subject.getPrincipals();
        //也是获取PrimaryPrincipal
        Object primaryPrincipal2=principalCollection.getPrimaryPrincipal();

        //获取所有身份验证成功的Realm名字
        Set<String> realmNames=principalCollection.getRealmNames();
        for(String realmName:realmNames)
            System.out.println(realmName);

        //将身份信息转换为Set/List（实际转换为List也是先转为Set）
        List<Object> principals=principalCollection.asList();
        /*返回集合包含两个String类、一个User类，但由于两个String类都是"zhang"，
        所以只只剩下一个,转为List结果也是一样*/
        for(Object principal:principals)
            System.out.println("set:"+principal);

        //根据realm名字获取身份，因为realm名字可以重复，所以可能有多个身份，建议尽量不要重复
        Collection<User> users=principalCollection.fromRealm("c");
        for(User user:users)
            System.out.println("c:user="+user.getUsername()+user.getPassword());
        Collection<String> usernames=principalCollection.fromRealm("b");
        for(String username:usernames)
            System.out.println("b:username="+username);
    }















































}
