package com.haien.chapter17.web.controller;

import com.haien.chapter17.Constants;
import com.haien.chapter17.service.OAuthService;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author haien
 * @Description 充当资源服务器，拥有user的信息，根据token给客户端提供这些信息
 * @Date 2019/3/28
 **/
@RestController
public class ResourceController {
    @Resource
    private OAuthService oAuthService;

    @RequestMapping("/userInfo")
    public HttpEntity userInfo(HttpServletRequest request) throws OAuthSystemException {
        try {
            //1. 将request包装成OAuth请求
            OAuthAccessResourceRequest oAuthAccessResourceRequest=
                    new OAuthAccessResourceRequest(request,ParameterStyle.QUERY);

            //2. 检查Access Token是否正确
            String accessToken=oAuthAccessResourceRequest.getAccessToken();
            if(!oAuthService.checkAccessToken(accessToken)){ //不存在或已过期
                //生成错误响应
                OAuthResponse response=OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(Constants.RESOURCE_SERVER_NAME)
                        .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                        .buildHeaderMessage();
                HttpHeaders headers=new HttpHeaders();
                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                        response.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity(headers,HttpStatus.UNAUTHORIZED);
            }

            //3. 返回资源（这里是用户名）
            String username=oAuthService.getUsernameByAccessToken(accessToken);
            return new ResponseEntity(username,HttpStatus.OK);
        } catch (OAuthProblemException e) {
            //是否设置了错误码
            String errorCode=e.getError();
            //没有错误码则不加错误码
            if(OAuthUtils.isEmpty(errorCode)){
                OAuthResponse response=OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(Constants.RESOURCE_SERVER_NAME)
                        .buildHeaderMessage();
                HttpHeaders headers=new HttpHeaders();
                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                        response.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity(headers,HttpStatus.UNAUTHORIZED);
            }

            //有就加上
            OAuthResponse response=OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(Constants.RESOURCE_SERVER_NAME)
                    .setError(e.getError())
                    .setErrorDescription(e.getDescription())
                    .setErrorUri(e.getUri())
                    .buildHeaderMessage();
            HttpHeaders headers=new HttpHeaders();
            headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                    response.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}










































