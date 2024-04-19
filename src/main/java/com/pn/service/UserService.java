package com.pn.service;

import com.pn.entity.Result;
import com.pn.entity.User;
import com.pn.page.Page;

import java.util.List;

public interface UserService {

    //根据用户名来查询用户账号的方法
    public User findUserByCode(String userCode);

    //分页查询用户账号的业务方法
    public Page queryUserPage(Page page,User user);

    //添加用户账号的方法
    public Result saveUser(User user);

    //修改用户账号的状态的业务方法
    public Result updateUserState(User user);

    //根据用户ids来删除用户的业务方法
    public Result removeUserByIds(List<Integer> userIdList);

    //根据用户id来修改用户昵称的业务方法
    public Result updateUserName(User user);

    //根据用户id来重置当前选中账号的密码
    public Result resetPwd(Integer userId);

}
