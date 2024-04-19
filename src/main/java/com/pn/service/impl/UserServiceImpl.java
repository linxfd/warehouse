package com.pn.service.impl;

import com.pn.entity.Result;
import com.pn.entity.User;
import com.pn.mapper.UserMapper;
import com.pn.page.Page;
import com.pn.service.UserService;
import com.pn.utils.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//将Service里面的bean注入到Spring的容器中
@Service
public class UserServiceImpl implements UserService {

    //注入UserMapper
    @Autowired
    private UserMapper userMapper;

    //根据用户名来查找用户账号的业务方法
    @Override
    public User findUserByCode(String userCode) {
        return userMapper.findUserByCode(userCode);
    }

    //分页查询用户账号的业务方法
    @Override
    public Page queryUserPage(Page page, User user) {
        //查询用户的总行数
        int userCount = userMapper.selectUserCount(user);
        //分页查询用户的账号
        List<User> userList = userMapper.selectUserPage(page,user);
        //将查询到的总行数和当前页数据组装到Page对象中
        page.setTotalNum(userCount);
        page.setResultList(userList);
        return page;
    }

    //添加用户账号的业务方法
    @Override
    public Result saveUser(User user) {
        //判断需要添加的账号，数据库表里面，是否已经存在了
        User oldUser = userMapper.findUserByCode(user.getUserCode());
        //要添加的账号，已经存在了
        if(oldUser != null){
            return Result.err(Result.CODE_ERR_BUSINESS,"用户账号已经存在了，不能重复添加!");
        }
        //当用户不存在时，需要先对密码进行加密处理
        String userPwd = DigestUtil.hmacSign(user.getUserPwd());
        //保存
        user.setUserPwd(userPwd);
        //添加
        userMapper.insertUser(user);
        return Result.ok("添加用户的账号成功!");
    }

    //修改用户账号的状态的业务方法
    @Override
    public Result updateUserState(User user) {
        //根据用户的id来修改用户的状态
        int i = userMapper.updateUserState(user);
        //修改成功
        if (i > 0) {
            return Result.ok("用户状态修改成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"用户状态修改失败!");
    }

    //根据用户的ids来删除用户的账号的业务方法
    @Override
    public Result removeUserByIds(List<Integer> userIdList) {
        //根据用户的ids来修改用户的状态为删除的状态
        int i = userMapper.setIsDeleteByUids(userIdList);
        //删除成功
        if(i > 0){
            return Result.ok("用户账号删除成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"用户账号删除失败!");
    }

    //根据用户id来修改用户昵称的业务方法
    @Override
    public Result updateUserName(User user) {
        //根据id来修改用户昵称
        int i = userMapper.updateNameById(user);
        //修改成功
        if(i > 0){
            return Result.ok("用户昵称修改成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"用户昵称修改失败!");
    }

    //重置密码
    @Override
    public Result resetPwd(Integer userId) {
        //创建User对象，并且保存用户id以及加密之后密码
        User user = new User();
        user.setUserId(userId);
        user.setUserPwd(DigestUtil.hmacSign("123456"));
        //重置密码
        int i = userMapper.updatePwdById(user);
        //密码修改成功
        if(i > 0){
            return Result.ok("密码重置成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"密码重置失败!");
    }

}
