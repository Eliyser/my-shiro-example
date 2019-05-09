package com.haien.chapter21.dao;

import java.util.List;

public interface UserRunAsDao {

    /**
     * @Author haien
     * @Description 授予身份，实际就是新增记录
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return void
     **/
    public void grantRunAs(Long fromUserId, Long toUserId);

    /**
     * @Author haien
     * @Description 回收身份，实际就是删除记录
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return void
     **/
    public void revokeRunAs(Long fromUserId, Long toUserId);

    /**
     * @Author haien
     * @Description 关系存在判断，实际就是查找
     * @Date 2019/4/10
     * @Param [fromUserId, toUserId]
     * @return boolean
     **/
    public boolean exists(Long fromUserId, Long toUserId);

    public List<Long> findFromUserIds(Long toUserId);
    public List<Long> findToUserIds(Long fromUserId);

}
