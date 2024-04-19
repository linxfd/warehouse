package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

    //权限菜单id
    private int authId;
    //父权限菜单id
    private int parentId;
    //权限菜单的名称
    private String authName;
    //权限菜单描述
    private String authDesc;
    //权限菜单的层级
    private int authGrade;
    //权限菜单的类型
    private String authType;
    //权限菜单访问的url接口
    private String authUrl;
    //权限菜单的标识
    private String authCode;
    //权限菜单的优先级
    private int authOrder;
    //权限菜单的状态
    private String authState;
    //权限菜单的创建人id
    private int createBy;
    //权限菜单的创建时间
    private Date createTime;
    //权限菜单的修改人id
    private int updateBy;
    //权限菜单的修改时间
    private Date updateTime;
    //追加的属性 -- List<Auth>集合 -- 用于储存当前权限下的所有菜单
    private List<Auth> childAuth;

}
