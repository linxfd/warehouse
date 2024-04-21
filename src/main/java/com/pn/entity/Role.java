package com.pn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    //角色id
    private int roleId;
    //角色名称
    private String roleName;
    //角色描述
    private String roleDesc;
    //
    private String roleCode;
    //角色状态
    private String roleState;
    //创建人
    private int createBy;
    //创建时间
    private Date createTime;
    //修改人
    private int updateBy;
    //修改时间
    private Date updateTime;


}
