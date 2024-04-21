package com.pn.dto;

import com.pn.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 */
@Data
public class UserRoleDTO {
    //用户id
    private int userId;
    //账号
    private String userCode;
    //角色集合
    List<Role> roleList;
    //角色选中集合(角色的名字roleName)
    List<String> roleCheckList;

}
