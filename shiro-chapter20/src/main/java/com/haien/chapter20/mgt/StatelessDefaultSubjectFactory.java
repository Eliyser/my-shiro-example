package com.haien.chapter20.mgt;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * @Author haien
 * @Description 禁止建立会话;否则在创建Subject时会自动创建Session
 * @Date 2019/4/8
 **/
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context){
        //不创建session,之后调用Subject.getSession()将抛出DisabledSessionException
        context.setSessionCreationEnabled(false);

        return super.createSubject(context);
    }
}
