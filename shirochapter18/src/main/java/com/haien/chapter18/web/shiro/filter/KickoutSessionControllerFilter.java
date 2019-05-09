package com.haien.chapter18.web.shiro.filter;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class KickoutSessionControllerFilter extends AccessControlFilter {
    //踢出后重定向的地址
    private String kickoutUrl;
    //是否踢出之后登录的用户，默认false，即踢出之前登录的而用户
    private boolean kickoutAfter=false;
    //同一账号最大会话数，默认1
    private int maxSession=1;
    //根据会话id，获取会话进行踢出操作
    private SessionManager sessionManager;
    //使用cacheManager获取相应cache来缓存用户登录的会话，用于保存用户-会话之间的关系
    private Cache<String,Deque<Serializable>> cache;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        Subject subject=getSubject(request,response);

        //如果未登录，则直接进行之后的流程
        if(!subject.isAuthenticated()&&!subject.isRemembered())
            return true; //打断点，看到底和false什么区别！！！

        Session session=subject.getSession();
        Serializable sessionId=session.getId();
        String username=(String)subject.getPrincipal();

        //获取之前登录者队列
        Deque<Serializable> deque=cache.get(username); //若有多人登录，则查询key为username的一条目，其value为包含多个会话的队列
        if(deque==null){
            deque=new LinkedList<>();
            //第一次暂时存储了一个空队列进去，但下一个if将放入会话，所以第一次存储了一个包含自身会话的队列
            cache.put(username,deque); //存储时键为username，值为之前登录者会话队列
        }

        //如果队列里没有此sessionId，且用户没有被踢出，则放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout")==null)
            deque.push(sessionId); //插入队头

        //如果队列里的会话数超出最大值，则开始踢人
        while (deque.size()>maxSession){ //用if应该就可以，因为顶多加入当前用户即超出限制
            //要被踢的sessionId
            Serializable kickoutSessionId=null;
            //如果规定踢出后者
            if(kickoutAfter)
                kickoutSessionId=deque.removeFirst(); //弹出首元素，此处为最后进去的元素
            //否则踢出前者
            else
                kickoutSessionId=deque.removeLast(); //弹出尾元素
            try {

                //根据session id获取session
                Session kickoutSession = sessionManager
                        .getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null)
                    //设置会话的kickout属性表示被踢出
                    kickoutSession.setAttribute("kickout", true);
            } catch (Exception e){
                //ignore
            }
        }

        //如果当前会话不幸在上面while中被踢出了，则注销并重定向到踢出后的地址；
        //但如果踢出的是其他会话，那么需要刷新，它才会被强制登出
        if(session.getAttribute("kickout")!=null){
            try {
                subject.logout();
            } catch (Exception e){
                //ignore
            }
            saveRequest(request); //保存当前请求
            WebUtils.issueRedirect(request,response,kickoutUrl); //返回login页面
            return false;
        }
        return true;
    }

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        //根据name获取cache实例，没有则新建一个
        this.cache = cacheManager.getCache("shiro-kickout-session");
    }
}

































