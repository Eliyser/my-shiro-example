package com.haien.chapter22.jcaptcha;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

/**
 * @Author haien
 * @Description 使用GMailEngine生成验证码图片、比对验证码
 * @Date 2019/4/19
 **/
public class MyManageableImageCaptchaService extends DefaultManageableImageCaptchaService {

    /**
     * @Author haien
     * @Description 构造器
     * @Date 16:08 2019/4/19
     * @Param [captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection]
     **/
    public MyManageableImageCaptchaService(CaptchaStore captchaStore,
                                           CaptchaEngine captchaEngine,
                                           int minGuarantedStorageDelayInSeconds,
                                           int maxCaptchaStoreSize,
                                           int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaStore,captchaEngine,minGuarantedStorageDelayInSeconds,
                maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
    }

    /**
     * @Author haien
     * @Description 根据会话id获取当前会话的验证码，与用户输入的比对
     * @Date 2019/4/11
     * @Param [id会话id, userCaptchaResponse用户输入的验证码]
     * @return boolean
     **/
    public boolean validate(String id,String userCaptchaResponse){
        return store.getCaptcha(id).validateResponse(userCaptchaResponse); //验证但不删除
    }
}































