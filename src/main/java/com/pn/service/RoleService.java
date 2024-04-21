package com.pn.service;

import com.pn.dto.AuthDTO;

import com.pn.dto.ExportDTO;
import com.pn.entity.Auth;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.entity.User;
import com.pn.page.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0
 */
public interface RoleService {

    // 查询所有角色
    List<Role> queryAllRole();

    // 分页查询角色
    Page queryRolePage(Page page, Role role);

    // 修改角色状态
    Result updateRoleState(Role role);

    // 保存角色
    Result saveRole(Role role);

    //根据用户ids来删除用户的业务方法
    public Result removerRoleByIds(List<Integer> roleIdList);

    List<Auth> findAuthTree(Integer roleId);

    //给角色授权
    Result saveRoleAuth(AuthDTO authDTO,Integer userId);


    void exportData(Page page, Role role, HttpServletResponse response);
}
