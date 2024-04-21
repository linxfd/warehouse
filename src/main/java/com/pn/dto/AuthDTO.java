package com.pn.dto;

import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 */
@Data
public class AuthDTO {
    //角色id
    private Integer roleId;
    //角色名称
    private String roleName;
    //角色描述
    private String roleDesc;
    //权限id集合
    private List<Integer> authIds;
}
