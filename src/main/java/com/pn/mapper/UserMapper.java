package com.pn.mapper;

import com.pn.entity.User;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    //根据用户名来查找用户信息的方法
    //在添加账号之前，需要通过此查询来判断，要添加的那个账号，数据库表里面是否已经存在了
    public User findUserByCode(String userCode);

    //查询用户总行数的方法
    public int selectUserCount(User user);

    //分页查询用户数据的方法
    public List<User> selectUserPage(@Param("page") Page page, @Param("user") User user);

    //添加用户账号的方法
    public int insertUser(User user);

    //根据用户的id来修改用户状态的方法
    public int updateUserState(User user);

    //根据用户id将用户的删除状态修改成1
    //将单删和批量删写在一个接口中
    public int setIsDeleteByUids(List<Integer> userIdList);

    //根据用户id来修改用户昵称的方法
    public int updateNameById(User user);

    //根据用户id来重置当前选中账号的密码
    public int updatePwdById(User user);

}
