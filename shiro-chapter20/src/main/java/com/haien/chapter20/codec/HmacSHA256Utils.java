package com.haien.chapter20.codec;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Map;

/**
 * @Author haien
 * @Description 消息摘要生成类
 * @Date 2019/4/7
 **/
public class HmacSHA256Utils {
    /**
     * @Author haien
     * @Description 使用指定的密码对string生成消息摘要
     * @Date 2019/4/7
     * @Param [key, content]
     * @return java.lang.String
     **/
    public static String digest(String key,String content){
        try {
            Mac mac=Mac.getInstance("HmacSHA256");
            byte[] secretByte=key.getBytes("utf-8");
            byte[] dataBytes=content.getBytes("utf-8");

            SecretKey secretKey=new SecretKeySpec(secretByte,"HMACSHA256");
            mac.init(secretKey);

            byte[] doFinal=mac.doFinal(dataBytes);
            byte[] hexB=new Hex().encode(doFinal);
            return new String(hexB,"utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Author haien
     * @Description 使用指定的密码对map生成消息摘要(底层是把map值取出来，连成字符串，调用第一个方法）
     * @Date 2019/4/8
     * @Param [key, content]
     * @return java.lang.String
     **/
    public static String digest(String key, Map<String,?> content){
        StringBuilder s=new StringBuilder();

        //遍历值的集合
        for(Object value:content.values()){
            if(value instanceof String[]){
                for(String str:(String[])value)
                    s.append(str);
            }else if(value instanceof List){
                for (String str:(List<String>)value)
                    s.append(str);
            }else{
                s.append(value);
            }
        }
        return digest(key,s.toString());
    }
}
























