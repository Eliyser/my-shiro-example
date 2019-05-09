package com.haien.shirochapter12.web.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author haien
 * @Description 全局异常处理类
 * @Date 2019/3/6
 **/
@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler({UnauthorizedException.class}) //处理controller抛出的该类异常及其子类
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView processUnauthenticatedException(NativeWebRequest request
            ,UnauthorizedException e){
        ModelAndView mv=new ModelAndView();
        mv.addObject("exception",e);
        mv.setViewName("unauthorized");
        return mv;
    }
}





















































