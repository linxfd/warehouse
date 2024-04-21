package com.pn.mapper;

import com.pn.dto.AuthDTO;

import com.pn.entity.Auth;
import com.pn.entity.Role;
import com.pn.entity.User;
import com.pn.page.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 */
@Mapper
public interface RoleMapper {
    //查询所有角色
    @Select("select * from role")
    List<Role> queryAllRole();

    //查询用户总行数的方法
    int selectRoleCount(Role role);

    //分页查询用户数据的方法
    List<Role> selectRolePage(@Param("page")Page page, @Param("role")Role role);


    //修改角色状态的方法

    int updateRoleState(Role role);

    //根据角色名查询角色的方法
    @Select("select * from role where role_name=#{roleName}")
    Role findRoleByName(Role role);

    //添加角色的方法
    @Update("insert into role(role_name,role_desc,role_code,role_state,create_by,create_time) " +
            "values(#{roleName},#{roleDesc},#{roleCode},0,#{createBy},now())")
    int insertRole(Role role);


    //根据角色id删除角色权限表的数据
    void deleteRoleAuthByRoleIds(List<Integer> roleIdList);

    //根据角色id集合删除角色的方法
    int deleteByRoleIds(List<Integer> roleIdList);

    //根据角色id集合查询角色表中不能被删除的数据
    int selectNotDeleteCount(List<Integer> roleIdList);

    //根据角色id删除角色用户表的数据
    void deleteRoleUserByRoleIds(List<Integer> roleIdList);

    //根据角色id查询权限树的方法
    List<Auth> findAuthTree(Integer roleId);

    //给角色授权
    int insertRoleAuth(AuthDTO authDTO);
}
