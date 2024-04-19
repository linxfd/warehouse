package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 储存用户登录的信息的类
 */
@Data   //相当于定义了getter/setter方法
@NoArgsConstructor  //相当于定义了无参构造函数
@AllArgsConstructor  //相当于定义了带参构造函数
@ToString  //相当于打印输出
public class LoginUser {

    //账号
    private String userCode;
    //密码
    private String userPwd;
    //验证码
    private String verificationCode;
    //用户的状态
    private String userState;

}
