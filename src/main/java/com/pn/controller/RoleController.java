package com.pn.controller;

import com.alibaba.fastjson.JSON;
import com.pn.dto.AuthDTO;

import com.pn.dto.ExportDTO;
import com.pn.entity.Auth;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.page.Page;
import com.pn.service.RoleService;
import com.pn.utils.CurrentUser;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;
    /**
     * 查询所有角色，供用户类型条件查询
     * @return
     */
    @GetMapping("/role-list")
    public Result roleList(){
        //执行业务-查询所有角色
        List<Role> roleList = roleService.queryAllRole();
        //响应
        return Result.ok(roleList);
    }


    /**
     * 分页查询用户
     * @param page 参数Page对象，用于接收请求参数页码pageNum、每一页的行数pageSize
     * @param role 参数role对象，用于接收请求参数角色名roleName、角色代码roleCode、角色状态roleState
     * @return
     */
    @GetMapping("/role-page-list")
    public Result userListPage(Page page,Role role){
        //执行业务
        page = roleService.queryRolePage(page,role);
        //响应
        return Result.ok(page);
    }

    /**
     * 修改角色
     * @param role
     * @param token
     * @return
     */
    @PutMapping("/role-state-update")
    public Result updateRoleState(@RequestBody Role role,
                                  @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取到当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取到修改人的id
        int updateBy = currentUser.getUserId();
        //保存
        role.setUpdateBy(updateBy);
        role.setUpdateTime(new Date());
        //执行修改业务
        Result result = roleService.updateRoleState(role);
        //响应
        return result;
    }

    @PostMapping("/role-add")
    public Result addRole(@RequestBody Role role,
                        @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        //获取到当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取到修改人的id
        int currentId  = currentUser.getUserId();
        //保存
        role.setCreateBy(currentId);
        //执行修改业务
        Result result = roleService.saveRole(role);
        //响应
        return result;
    }

    /**
     * 根据id删除角色
     * @param roleId
     * @return
     */
    @DeleteMapping("/role-delete/{roleId}")
    public Result deleteRole(@PathVariable Integer roleId){
        //执行业务
        Result result = roleService.removerRoleByIds(Arrays.asList(roleId));
        //响应
        return result;
    }

    @PutMapping("/role-update")
    public Result updateRole(@RequestBody AuthDTO authDTO){
        Role role = new Role();
        BeanUtils.copyProperties(authDTO,role);
        //执行业务
        Result result = roleService.updateRoleState(role);
        return result;
    }
    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     */
    @GetMapping("/role-auth")
    public Result AuthList(@RequestParam(required = false)Integer roleId){
        List<Auth> authList = roleService.findAuthTree(roleId);
        return Result.ok(authList);
    }

    /**
     * 给角色id授权权限
     * @param authDTO 里面有角色id和权限id集合
     * @return
     */
    @PutMapping("/auth-grant")
    public Result authGrant(@RequestBody AuthDTO authDTO,
                            @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String clientToken) {
        //从前端返回的token中，解析出当前登录的用户的信息
        CurrentUser currentUser = tokenUtils.getCurrentUser(clientToken);
        Integer userId = currentUser.getUserId();
        Result result = roleService.saveRoleAuth(authDTO,userId);
        return result;
    }

    /**
     * 导出数据
     * @param pageNum 当前页码
     * @param pageSize 每页显示的行数
     * @param roleCode 角色代码
     * @param roleName 角色名称
     * @param roleState 角色状态
     * @param totalNum 总行数
     * @param response 响应对象
     * @return
     */
    @GetMapping("/exportTable")
    public void exportTable(@RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize,
                              @RequestParam("roleCode") String roleCode,
                              @RequestParam("roleName") String roleName,
                              @RequestParam("roleState") String roleState,
                              @RequestParam("totalNum") Integer totalNum,
                                HttpServletResponse response){
        Page page = Page.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalNum(totalNum)
                .build();
        Role role = Role.builder()
                .roleCode(roleCode)
                .roleName(roleName)
                .roleState(roleState)
                .build();
        roleService.exportData(page, role,response);
//        return Result.ok();
    }
}
