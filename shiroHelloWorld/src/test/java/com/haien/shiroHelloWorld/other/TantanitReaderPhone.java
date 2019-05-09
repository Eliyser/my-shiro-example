package com.haien.shiroHelloWorld.other;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TantanitReaderPhone {
    private String areaCode;
    private String localNumber;

    public TantanitReaderPhone(String areaCode, String localNumber) {
        this.areaCode = areaCode;
        this.localNumber = localNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TantanitReaderPhone that = (TantanitReaderPhone) o;
        //区号、号码都相同即为相同
        boolean equal = Objects.equals(areaCode, that.areaCode) &&
                Objects.equals(localNumber, that.localNumber); //调用String类的equals方法判断字符串是否长一样
        return equal;
    }

    @Override
    public int hashCode() {
        int result=17;
        //若areaCode、localNumber两个字符串分别相同则返回hashCode相同
        result=31*result+areaCode.hashCode();
        result=31*result+localNumber.hashCode();
        return result;
    }

    public static void main(String[] args) {
        Map<TantanitReaderPhone,String> map=new HashMap<>();
        map.put(new TantanitReaderPhone("86","13200001234"),"张三");
        //key相同，张三被zhang覆盖；但不知道key和value同时和上面一样，最终是被覆盖还是不存储。
        map.put(new TantanitReaderPhone("86","13200001234"),"zhang");

        //获取键值对集合
        for(Map.Entry<TantanitReaderPhone,String> entry:map.entrySet())
            System.out.println(entry.getKey()+entry.getValue());
    }
}
