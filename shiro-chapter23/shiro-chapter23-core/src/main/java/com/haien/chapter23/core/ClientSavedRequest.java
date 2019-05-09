package com.haien.chapter23.core;

import org.apache.shiro.web.util.SavedRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author haien
 * @Description 继承SavedRequest，实现保存下来的原地址包含<协议>://<域名>:<端口>部分
 * @Date 2019/4/25
 **/
public class ClientSavedRequest extends SavedRequest {
    private String scheme;
    private String domain;
    private int port;
    private String contextPath;
    private String backUrl;

    public ClientSavedRequest(HttpServletRequest request, String backUrl) {
        super(request);
        this.scheme=request.getScheme(); //http
        this.domain=request.getServerName(); //localhost
        this.port=request.getServerPort(); //80
        this.backUrl = backUrl; //null
        //默认当前请求的上下文为原地址的上下文
        this.contextPath=request.getContextPath(); // /chapter23-app1
    }

    /**
     * @Author haien
     * @Description 将原地址恢复完整
     * @Date 2019/4/27
     * @Param []
     * @return java.lang.String
     **/
    @Override
    public String getRequestUrl(){
        String requestURI=getRequestURI(); //若跳转登录前要访问的是chapter23-app1/attr，则requestURI为此

        if(backUrl!=null){ //除非当前请求带了backUrl参数，否则都为null
            //完整路径直接返回即可
            if(backUrl.toLowerCase().startsWith("http://")
               || backUrl.toLowerCase().startsWith("https://")){
                return backUrl;
            } else if(!backUrl.startsWith(contextPath)) { //无上下文，先加上
                requestURI=contextPath+backUrl;
            } else{ //否则就是没有协议头，但是有上下文了
                requestURI=backUrl;
            }
        }

        //拼上协议和域名
        StringBuilder requestUrl=new StringBuilder(scheme);
        requestUrl.append("://");
        requestUrl.append(domain);

        //拼上端口
        if("http".equalsIgnoreCase(scheme) && port!=80){
            requestUrl.append(":").append(String.valueOf(port));
        } else if ("https".equalsIgnoreCase(scheme) && port!=443){
            requestUrl.append(":").append(String.valueOf(port));
        }

        //拼上原地址
        requestUrl.append(requestURI);

        //如果原地址没值？但有参数，则拼上参数
        if(backUrl==null && getQueryString()!=null)
            requestUrl.append("?").append(getQueryString());

        //否则不用拼参数，直接返回
        return requestUrl.toString();
    }

    public String getScheme() {
        return scheme;
    }

    public String getDomain() {
        return domain;
    }

    public int getPort() {
        return port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getBackUrl() {
        return backUrl;
    }
}






























