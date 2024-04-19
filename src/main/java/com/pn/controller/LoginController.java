package com.pn.controller;

import com.pn.entity.LoginUser;
import com.pn.entity.Result;
import com.pn.entity.User;
import com.pn.service.UserService;
import com.pn.utils.CurrentUser;
import com.pn.utils.DigestUtil;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    //注入UserService
    @Autowired
    private UserService userService;

    //注入redis模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    /**
     * 登录的url接口  /login
     *
     * @RequestBody:表示接收并且封装前端传递的登录的用户信息的json字符串
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginUser loginUser){
        //校验验证码
        if(!stringRedisTemplate.hasKey(loginUser.getVerificationCode())){
            return Result.err(Result.CODE_ERR_BUSINESS,"验证码不正确!");
        }
        //校验用户名和密码（此处不需要进行非空校验，原因是前端已经做了非空验证）
        //根据用户名查询用户
        User user = userService.findUserByCode(loginUser.getUserCode());
        //查询到了用户
        if(user != null){
            //查询到的用户状态是已经审核的状态
            if(user.getUserState().equals(WarehouseConstants.USER_STATE_PASS)){
                //对用户输入的密码进行加密处理
                String password = DigestUtil.hmacSign(loginUser.getUserPwd());
                //查询到数据后加密的密码与数据库表的加密的密码是一样的
                if(password.equals(user.getUserPwd())){
                    //生成currentUser对象
                    CurrentUser currentUser = new CurrentUser(user.getUserId(),user.getUserCode(),user.getUserName());
                    //生成token，并且返回给前端
                    String token = tokenUtils.loginSign(currentUser,user.getUserPwd());
                    //返回
                    return Result.ok("登录成功!",token);
                } else {
                    //查询到数据后加密的密码与数据库表的加密的密码是不一样的
                    return Result.err(Result.CODE_ERR_BUSINESS,"密码不一致!");
                }
            } else {
                //查询到的用户状态是未审核的状态
                return Result.err(Result.CODE_ERR_BUSINESS,"用户未审核!");
            }
        } else {
            //没有查询到用户
            return Result.err(Result.CODE_ERR_BUSINESS,"该用户不存在!");
        }
    }

    /**
     * 获取当前登录的用户信息的url接口  /curr-user
     */
    @GetMapping("/curr-user")
    public Result currUser(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String clientToken){
        //获取到当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(clientToken);
        //响应
        return Result.ok(currentUser);
    }

    /**
     * 退出登录的url接口  /logout
     */
    @DeleteMapping("/logout")
    public Result logout(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String clientToken){
        //从redis中移除token的键
        stringRedisTemplate.delete(clientToken);
        return Result.ok();
    }

}

