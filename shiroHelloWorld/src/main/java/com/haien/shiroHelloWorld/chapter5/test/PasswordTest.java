package com.haien.shiroHelloWorld.chapter5.test;

import com.haien.shiroHelloWorld.test.BaseTest;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.junit.Test;

public class PasswordTest extends BaseTest {
    @Test
    public void testPasswordServiceWithMyRealm(){
        login("classpath:config/shiro-passwordservice.ini",
                "wu","123");
    }

    @Test
    public void testPasswordserviceWithJdbcRealm(){
        login("classpath:config/shiro-jdbc-passwordservice.ini",
                "wu","123");
    }

    @Test
    public void testHashedCredentialsMatcherWithMyRealm2() {
        login("classpath:config/shiro-hashedCredentialsMatcher.ini",
                "liu", "123"); //HashedCredentialsMatcher会将密码加密后与Realm提供的用户库进行比对
    }

    /**
     * @Author haien
     * @Description 自定义Enum转换器
     * @Date 2019/2/23
     **/
    private class EnumConverter extends AbstractConverter{
        @Override
        protected String convertToString(final Object value) throws Throwable {
            return ((Enum)value).name();
        }

        @Override
        protected Class getDefaultType() {
            return null;
        }

        @Override
        protected Object convertToType(final Class type, final Object value)
                throws Throwable {
            return Enum.valueOf(type,value.toString());
        }
    }

    @Test
    public void testHashedCredentialsMatcherWithJdbcRealm(){
        //自己注册一个Enum转换器，否则ini文件默认不进行Enum类型转换
        BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(),
                JdbcRealm.SaltStyle.class);

        login("classpath:config/shiro-jdbc-hashedCredentialsMatcher.ini",
                "liu", "123");
    }
}




















