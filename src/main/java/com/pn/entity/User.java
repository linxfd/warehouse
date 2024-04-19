package com.pn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 该类是数据库user_info表所对应的实体类
 */
@Data
@ToString
public class User {

    //用户id
    private int userId;
    //账号
    private String userCode;
    //用户名
    private String userName;
    //用户密码
    private String userPwd;
    //用户的类型
    private String userType;
    //用户的状态
    private String userState;
    //删除的状态
    private String isDelete;
    //创建人id
    private int createBy;
    //创建时间
    //时间在返回前端时，自动将Date给转换成指定格式的json字符串
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //修改人的id
    private int updateBy;
    //修改时间
    private Date updateTime;
    //追加一个属性 -- 创建账号的人
    private String getCode;

    //无参构造函数
    public User() {
    }

    //带参构造函数
    public User(int userId, String userCode, String userName,
                String userPwd, String userType, String userState,
                String isDelete, int createBy, Date createTime,
                int updateBy, Date updateTime) {
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userType = userType;
        this.userState = userState;
        this.isDelete = isDelete;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
    }
}
