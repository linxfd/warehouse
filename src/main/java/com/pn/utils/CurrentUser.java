package com.pn.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 此类只封装了用户的用户id、用户名以及用户的真实姓名
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CurrentUser {

    //用户id
    private int userId;
    //用户名
    private String userCode;
    //用户真实姓名
    private String userName;

}
