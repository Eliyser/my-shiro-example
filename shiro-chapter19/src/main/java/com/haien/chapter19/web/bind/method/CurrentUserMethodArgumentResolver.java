package com.haien.chapter19.web.bind.method;

import com.haien.chapter19.web.bind.annotation.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author haien
 * @Description 用于解析自定义注解@CurrentUser的方法参数解析器
 * @Date 2019/3/14
 **/
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public CurrentUserMethodArgumentResolver() {
    }

    /**
     * @Author haien
     * @Description 判断参数是否受支持，依据是它是否拥有CurrentUser注解
     * @Date 2019/3/14
     * @Param [parameter]
     * @return boolean
     **/
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //判断参数是否被注解了
        if(parameter.hasParameterAnnotation(CurrentUser.class)){
            return true;
        }
        return false;
    }

    /**
     * @Author haien
     * @Description 对被注解参数的解析是：获取当前登录对象并返回给此参数
     * @Date 2019/3/14
     * @Param [parameter, mavContainer, webRequest, binderFactory]
     * @return java.lang.Object
     **/
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
           NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        CurrentUser currentUserAnnotation=parameter.getParameterAnnotation(CurrentUser.class);
        //currentUserAnnotation.value()返回“user”，从request获取“user”属性
        return webRequest.getAttribute(currentUserAnnotation.value(),
                NativeWebRequest.SCOPE_REQUEST);
    }
}


































