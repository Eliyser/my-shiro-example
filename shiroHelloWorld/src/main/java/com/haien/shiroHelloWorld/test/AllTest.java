package com.haien.shiroHelloWorld.test;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import java.security.Key;

/**
 * @Author haien
 * @Description 各种小测试
 * @Date 2019/2/22
 **/
public class AllTest {
    public static void main(String[] args) {
        DefaultHashService hashService = new DefaultHashService(); //默认实现
        //all设置
        hashService.setHashAlgorithmName("SHA-512"); //设置算法；默认为SHA-512;被request覆盖
        hashService.setPrivateSalt(new SimpleByteSource("123")); //私盐，散列时自动与用户传入的公盐混合产生一个新盐；默认无
        hashService.setGeneratePublicSalt(true); //在用户没有传入公盐时是否自动产生公盐；被request覆盖
        hashService.setRandomNumberGenerator(new SecureRandomNumberGenerator()); //用于生成公盐；默认就这个
        hashService.setHashIterations(1); //散列迭代次数;被request覆盖

        HashRequest request=new HashRequest.Builder() //all有用配置如下
                .setAlgorithmName("MD5").setSource(ByteSource.Util.bytes("hello"))
                .setSalt(ByteSource.Util.bytes("123")).setIterations(2).build();

        String hex=hashService.computeHash((request)).toHex();

        AesCipherService aesCipherService=new AesCipherService();
        aesCipherService.setKeySize(128); //设置key长度；不影响加密后长度
        Key key=aesCipherService.generateNewKey(); //生成key
        String source="hello";
        String encryptText=aesCipherService.encrypt(source.getBytes(),key.getEncoded())
                .toHex(); //加密，参数是两个byte数组
        String source2=new String(aesCipherService.decrypt(Hex.decode(encryptText),
                key.getEncoded()).getBytes()); //解密,参数是两个byte数组

        Hash hash=new SimpleHash("MD5", new SimpleByteSource("123"),
                new SimpleByteSource("www"),2);
        System.out.println(hash.toString()); //9bfe7c447c6c58389824bd8f1719b0bb
        System.out.println(hash.toHex()); //9bfe7c447c6c58389824bd8f1719b0bb
        System.out.println(hash.toBase64()); //m/58RHxsWDiYJL2PFxmwuw==

        byte[] bytes=new byte[]{65,66};
        System.out.println(bytes.toString());

        String algorithmName = "md5";
        String username = "liu";
        String password = "123";
        String salt1 = username;
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex(); //每次执行都不一样
        int hashIterations = 2;

        SimpleHash hash1 = new SimpleHash(algorithmName, password, salt1 + salt2,
                hashIterations);
        String encodedPassword = hash1.toHex();
        System.out.println(encodedPassword); //每次执行都不一样
    }
}


























