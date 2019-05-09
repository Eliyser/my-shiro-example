package com.haien.shiroChapter11.service;

import com.haien.shiroChapter11.entity.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @Author haien
 * @Description 指定用MD5加密两次进行密码加密
 * @Date 2019/2/24
 **/
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator=new SecureRandomNumberGenerator();
    private String algorithmName="md5";
    private final int hashIterations=2;

    public void encryptPassword(User user){
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword=new SimpleHash(algorithmName, user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),hashIterations).toHex();
        user.setPassword(newPassword);
    }
}
