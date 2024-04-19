package com.pn.service;

import com.pn.entity.Auth;

import java.util.List;

public interface AuthService {

    //根据用户的id来查询用户权限菜单树的业务方法
    public List<Auth> findAuthTree(int userId);

}
