package com.pn.mapper;

import com.pn.entity.Auth;

import java.util.List;

public interface AuthMapper {

    //根据用户的id来查询用户所有权限菜单的方法
    public List<Auth> findAllAuth(int userId);

}
