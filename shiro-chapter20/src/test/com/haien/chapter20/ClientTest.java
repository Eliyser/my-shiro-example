package com.haien.chapter20;

import com.haien.chapter20.codec.HmacSHA256Utils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Author haien
 * @Description 模拟来自客户端的请求
 * @Date 2019/4/9
 **/
public class ClientTest {
    private static Server server;
    private RestTemplate restTemplate=new RestTemplate();

    /**
     * @Author haien
     * @Description 在整个测试开始之前启动服务器
     * @Date 2019/4/9
     * @Param
     * @return
     **/
    @BeforeClass //在整个测试开始之前执行
    public static void beforeClass() throws Exception {
        //创建一个Server
        server=new Server(8080);

        WebAppContext context=new WebAppContext();
        String webappPath="./src/main/webapp";
        context.setDescriptor(webappPath+"/WEB/INF/web.xml"); //指定web.xml配置文件
        context.setResourceBase(webappPath); //指定webapp目录
        context.setContextPath("/");
        context.setParentLoaderPriority(true);

        server.setHandler(context);
        server.start();
    }

    /**
     * @Author haien
     * @Description 测试结束后停止服务器
     * @Date 2019/4/9
     * @Param []
     * @return void
     **/
    @AfterClass
    public static void afterClass() throws Exception {
        server.stop();
    }

    /**
     * @Author haien
     * @Description 测试成功情况
     * @Date 2019/4/9
     * @Param []
     * @return void
     **/
    @Test
    public void testServiceHelloSuccess(){
        String username="admin";
        String param11="param11";
        String param12="param12";
        String param2="param2";
        String key="dadadswdewq2ewdwqdwadsadasd"; //和服务端使用的key一致

        //参数必须按照如下顺序，否则客户端会接收不到某个参数
        MultiValueMap<String,String> params=new LinkedMultiValueMap<>();
        params.add("param1",param11);
        params.add("param1",param12);
        params.add(Constants.PARAM_USERNAME,username);
        params.add("param2",param2);
        params.add(Constants.PARAM_DIGEST,HmacSHA256Utils.digest(key,params)); //和服务端生成摘要方式一致

        //构造url；UriComponentsBuilder需要spring-web依赖
        String url=UriComponentsBuilder.fromHttpUrl("http://localhost:8080/hello")
                .queryParams(params).build().toUriString();
        //发起请求并获取响应，指定响应体映射为string对象
        ResponseEntity responseEntity=restTemplate.getForEntity(url,String.class);

        Assert.assertEquals("hello"+param11+param2,responseEntity.getBody());
    }

    /**
     * @Author haien
     * @Description 测试失败情况
     * @Date 2019/4/9
     * @Param []
     * @return void
     **/
    @Test
    public void testServiceHelloFail(){
        String username="admin";
        String param11="param11";
        String param12="param12";
        String param2="param2";
        String key="dadadswdewq2ewdwqdwadsadasd"; //和服务端使用的key一致

        MultiValueMap<String,String> params=new LinkedMultiValueMap<>();
        params.add("param1",param11);
        params.add("param1",param12);
        params.add(Constants.PARAM_USERNAME,username);
        params.add("param2",param2);
        params.add(Constants.PARAM_DIGEST,HmacSHA256Utils.digest(key,params)); //和服务端生成摘要方式一致
        //篡改请求参数
        params.set("param2",param2+"1");

        //构造url；UriComponentsBuilder需要spring-web依赖
        String url=UriComponentsBuilder.fromHttpUrl("http://localhost:8080/hello")
                .queryParams(params).build().toUriString();
        //发起请求并获取响应，指定响应体映射为string对象
        try {
            ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e){
            Assert.assertEquals(HttpStatus.UNAUTHORIZED,e.getStatusCode());
            Assert.assertEquals("login error",e.getResponseBodyAsString());
        }
    }
}





























