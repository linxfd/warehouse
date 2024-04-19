package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 该类可以封装当前这个项目的所有方法的返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Result {

    /**
     * 状态码的常量
     */
    //成功
    public static final int CODE_OK = 200;
    //业务错误
    public static final int CODE_ERR_BUSINESS = 501;
    //用户未登录
    public static final int CODE_ERR_UNLOGINED = 502;
    //系统出错
    public static final int CODE_ERR_SYS = 503;

    //成员属性（将以下4个成员属性封装成json字符串，然后响应给前端）
    //状态码
    private int code;
    //成功则响应结果为true,失败则响应为false
    private boolean success;
    //响应的信息
    private String message;
    //响应数据
    private Object data;

    //成功响应的方法 -- 返回的Result中只封装了成功的状态码
    public static Result ok(){
        return new Result(CODE_OK,true,null,null);
    }

    //成功响应的方法 -- 返回的Result中封装了成功的状态码和响应的信息
    public static Result ok(String message){
        return new Result(CODE_OK,true,message,null);
    }

    //成功响应的方法 -- 返回的Result中封装了成功的状态码和响应的数据
    public static Result ok(Object data){
        return new Result(CODE_OK,true,null,data);
    }

    //成功响应的方法 -- 返回的Result中封装了成功的状态码和响应的信息以及响应的数据
    public static Result ok(String message,Object data){
        return new Result(CODE_OK,true,message,data);
    }

    //失败响应的方法 -- 返回的Result中封装了失败的状态码和响应的信息
    public static Result err(int errCode,String message){
        return new Result(errCode,false,message,null);
    }

    //失败响应的方法 -- 返回的Result中封装了失败的状态码和响应的信息以及响应的数据
    public static Result err(int errCode,String message,Object data){
        return new Result(errCode,false,message,data);
    }

}
