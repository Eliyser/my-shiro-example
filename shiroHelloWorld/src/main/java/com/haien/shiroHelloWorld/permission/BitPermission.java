package com.haien.shiroHelloWorld.permission;

import com.alibaba.druid.util.StringUtils;
import org.apache.shiro.authz.Permission;


/**
 * @Author haien
 * @Description 实现位移方式的权限定义，解析要求的权限。被BitAndWildPermissionResolver调用。
 *              规则：+资源+权限位。以+开头，中间通过+分隔；
 *              权限位：0-all权限（二进制0000），1-新增（0001），2-修改（0010），
 *              4-删除（0100）、8-查看（1000）；如，+user+10，由于10二进制为1010,
 *              1位置与2、8相同，按位与返回二进制数非0，所以表示拥有修改、查看权限
 * @Date 2019/2/18
 **/
public class BitPermission implements Permission {
    //资源
    private String resourceIdentify;
    //权限位
    private int permissionBit;
    //实例id
    private String instanceId;

    public BitPermission(String permissionString) {
        //解析要求的权限字符串
        String[] array=permissionString.split("\\+"); //加//防止+被解析
        if(array.length>1){
            resourceIdentify=array[1]; //第0个为空
        }
        if(StringUtils.isEmpty(resourceIdentify)){ //开头两个+相连，或者length<=1,即根本没定义权限
            resourceIdentify="*";
        }
        if(array.length>2){
            permissionBit=Integer.valueOf(array[2]);
        }//peimissionBit为空的话即默认值0，不用赋*给它，后面用0来判断即可
        if(array.length>3){
            instanceId=array[3];
        }
        if(StringUtils.isEmpty(instanceId)){
            instanceId="*";
        }
    }

    /**
     * @Author haien
     * @Description 判断用户是否拥有权限
     * @Date 2019/2/18
     * @Param [p所需权限](this为用户权限)
     * @return boolean
     **/
    @Override
    public boolean implies(Permission p) {
        if(!(p instanceof BitPermission)) {
            return false;
        }

        BitPermission other=(BitPermission)p;
        if(!("*".equals(this.resourceIdentify) //this：用户拥有的权限，other：所需权限
             ||this.resourceIdentify.equals(other.resourceIdentify))){
            return false;
        }
        if(!(this.permissionBit==0||(this.permissionBit&other.permissionBit)!=0)){
            return false;
        }
        if(!("*".equals(this.instanceId)||this.instanceId.equals(other.instanceId))){
            return false;
        }
        return true;
    }
}























