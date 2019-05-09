package com.haien.chapter21.service.impl;

import com.haien.chapter21.dao.UserRunAsDao;
import com.haien.chapter21.service.UserRunAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userRunAsService")
public class UserRunAsServiceImpl implements UserRunAsService {
    @Autowired
    private UserRunAsDao userRunAsDao;

    /**
     * @Author haien
     * @Description 授予身份，实际就是新增记录
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return void
     **/
    @Override
    public void grantRunAs(Long fromUserId, Long toUserId) {
        userRunAsDao.grantRunAs(fromUserId, toUserId);
    }

    /**
     * @Author haien
     * @Description 回收身份，实际就是删除记录
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return void
     **/
    @Override
    public void revokeRunAs(Long fromUserId, Long toUserId) {
        userRunAsDao.revokeRunAs(fromUserId, toUserId);
    }

    /**
     * @Author haien
     * @Description 关系存在判断，实际就是查找
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return boolean
     **/
    @Override
    public boolean exists(Long fromUserId, Long toUserId) {
        return userRunAsDao.exists(fromUserId, toUserId);
    }

    /**
     * @Author haien
     * @Description 根据秘书查找其代理的老板
     * @Date 2019/4/10
     * @Param [toUserId]
     * @return java.util.List<java.lang.Long>
     **/
    @Override
    public List<Long> findFromUserIds(Long toUserId) {
        return userRunAsDao.findFromUserIds(toUserId);
    }

    /**
     * @Author haien
     * @Description 根据老板查找其委托的秘书
     * @Date 2019/4/10
     * @Param [fromUserId]
     * @return java.util.List<java.lang.Long>
     **/
    @Override
    public List<Long> findToUserIds(Long fromUserId) {
        return userRunAsDao.findToUserIds(fromUserId);
    }
}
