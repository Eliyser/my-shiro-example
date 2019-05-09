package com.haien.chapter22.jcaptcha;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Author haien
 * @Description 获取验证码图片并写到客户端的Spring过滤器
 * @Date 2019/4/11
 **/
public class JCaptchaFilter extends OncePerRequestFilter { //保证只执行一次，注：继承的是spring的Filter

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        //响应内容不缓存
        response.setDateHeader("Expires",0L);
        response.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
        response.addHeader("Cache-Control","post-check=0,pre-check=0");
        response.setHeader("Pragma","no-cache");
        response.setContentType("image/jpeg");

        String id=request.getRequestedSessionId();
        //captchaService使用当前会话的id作为key获取相应的验证码图片
        BufferedImage bi=JCaptcha.captchaService.getImageChallengeForID(id);
        ServletOutputStream out=response.getOutputStream();
        ImageIO.write(bi,"jpg",out);
        try {
            out.flush();
        }finally {
            out.close();
        }
    }
}












































