package com.haien.chapter22.jcaptcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author haien
 * @Description jcaptcha工具类，调用API生成、验证验证码
 * @Date 2019/4/19
 **/
public class JCaptcha {
    public static final MyManageableImageCaptchaService captchaService=
            new MyManageableImageCaptchaService(new FastHashMapCaptchaStore(),
                    new GMailEngine(),180,
                    100000,75000);

    /**
     * @Author haien
     * @Description 比对当前请求输入的验证码是否正确，并从CaptchaService中移除已使用过的验证码
     * @Date 2019/4/11
     * @Param [request, userCaptchaResponse用户输入的验证码]
     * @return boolean
     **/
    public static boolean validateResponse(HttpServletRequest request,String userCaptchaResponse){
        //为true应该是没有session则新建
        if(request.getSession(false)==null) return false;

        boolean validated=false;
        try {
            //获取会话id
            String id = request.getSession().getId();
            //将用户输入的验证码与库存比对，并移除验证码与id（无论正确与否）
            validated = captchaService.validateResponseForID(id, userCaptchaResponse)
                    .booleanValue();
        }catch (CaptchaServiceException e){
            e.printStackTrace();
        }

        return validated;
    }

    /**
     * @Author haien
     * @Description 比对，但不移除验证码，适用于Ajax异步验证，此时不是真的验证，还不能删除验证码
     * @Date 2019/4/11
     * @Param [request, userCaptchaResponse]
     * @return boolean
     **/
    public static boolean validate(HttpServletRequest request,String userCaptchaResponse){
        if (request.getSession(false)==null) return false;

        boolean validated=false;
        try {
            String id=request.getSession().getId();
            validated=captchaService.validate(id,userCaptchaResponse);
        }catch (CaptchaServiceException e){
            e.printStackTrace();
        }

        return validated;
    }
}








































