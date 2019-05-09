package com.haien.chapter21.web.bind.annotation;

import com.haien.chapter21.Constants;

import java.lang.annotation.*;

/**
 * @Author haien
 * @Description 绑定当前登录的用户
 * @Date 2019/3/14
 **/
//测试此注解作用
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
    String value() default Constants.CURRENT_USER;
}
