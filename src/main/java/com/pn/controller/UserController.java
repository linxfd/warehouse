package com.pn.controller;

import com.pn.dto.AuthDTO;
import com.pn.dto.UserRoleDTO;
import com.pn.entity.Auth;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.entity.User;
import com.pn.page.Page;
import com.pn.service.AuthService;
import com.pn.service.UserService;
import com.pn.utils.CurrentUser;
import com.pn.utils.TokenUtils;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {

    //注入AuthService
    @Autowired
    private AuthService authService;

    //注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    //注入UserService
    @Autowired
    private UserService userService;

    /**
     * 加载当前登录的用户的权限菜单树的url接口  /auth-list
     */
    @GetMapping("/auth-list")
    public Result authList(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String clientToken){
        //从前端返回的token中，解析出当前登录的用户的信息
        CurrentUser currentUser = tokenUtils.getCurrentUser(clientToken);
        //根据用户的id来查询用户权限的菜单树
        List<Auth> authTreeList = authService.findAuthTree(currentUser.getUserId());
        //响应
        return Result.ok(authTreeList);
    }

    /**
     * 分页查询用户的url接口  /user-list
     *
     * 参数Page对象，用于接收请求参数页码pageNum、每一页的行数pageSize
     * 参数User对象，用于接收请求参数用户名userCode、用户类型userType、用户状态userState
     */
    @GetMapping("/user-list")
    public Result userListPage(Page page, User user){
        //执行业务
        page = userService.queryUserPage(page,user);
        //响应
        return Result.ok(page);
    }

    /**
     * 添加用户账号的url接口  /addUser
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user,
                          @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取当前登录的用户的信息
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取当前登录的用户id
        int createBy = currentUser.getUserId();
        //保存
        user.setCreateBy(createBy);
        //执行业务
        Result result = userService.saveUser(user);
        //响应
        return result;
    }

    /**
     * 修改用户状态的url接口  /user/updateState
     */
    @PutMapping("/updateState")
    public Result updateUserState(@RequestBody User user,
                                  @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取到当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取到修改人的id
        int updateBy = currentUser.getUserId();
        //保存
        user.setUpdateBy(updateBy);
        user.setUpdateTime(new Date());
        //执行修改业务
        Result result = userService.updateUserState(user);
        //响应
        return result;
    }

    /**
     * （单删）删除用户的url接口  /user/deleteUser/{userId}
     */
    @DeleteMapping("/deleteUser/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        //执行业务
        //Arrays是数组的工具类
        Result result = userService.removeUserByIds(Arrays.asList(userId));
        //响应
        return result;
    }

    /**
     * 批量删除用户的url接口  /user/deleteUserList
     */
    @DeleteMapping("/deleteUserList")   //restful风格
    public Result deleteUserList(@RequestBody List<Integer> userIdList){
        //执行业务
        Result result = userService.removeUserByIds(userIdList);
        //响应
        return result;
    }

    /**
     * 修改用户昵称的url接口  /user/updateUser
     */
    @RequestMapping("/updateUser")
    public Result updateUser(@RequestBody User user,
                             @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token){
        //获取当前登录的用户
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        //获取修改昵称的用户id
        int updateBy = currentUser.getUserId();
        //保存
        user.setUpdateBy(updateBy);
        //执行业务
        Result result = userService.updateUserName(user);
        //响应
        return result;
    }

    /**
     * 重置密码的url接口  /user/updatePwd/{userId}
     */
    @PutMapping("/updatePwd/{userId}")
    public Result resetPassword(@PathVariable Integer userId){
        Result result = userService.resetPwd(userId);
        return result;
    }

    /**
     * 根据用户id查询角色的url接口  /user/user-role-list/{userId}
     * @param userId
     * @return
     */
    @GetMapping("/user-role-list/{userId}")
   public Result findRoleByUserId(@PathVariable Integer userId){

        List<Role> roleList = userService.findRoleByUserId(userId);
        return Result.ok(roleList);
   }

   //根据用户id来给用户分配角色的业务方法
   @PutMapping("/assignRole")
   public Result assignRole(@RequestBody UserRoleDTO userRoleDTO){

       Result result=userService.assignRole(userRoleDTO);
        return result;
   }

    /**
     * 根据用户id查询权限
     * @param userId
     * @return
     */
    @GetMapping("/user-auth")
    public Result AuthList(@RequestParam(required = false)Integer userId){
        List<Integer> authList = userService.findAuthTree(userId);
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

//        Result result = userService.saveRoleAuth(authDTO,userId);
        return null;
    }


    /**
     * 导出数据
     * @param pageNum 当前页码
     * @param pageSize 每页显示的行数
     * @param userCode 用户代码
     * @param userType 用户名称
     * @param userState 用户状态
     * @param totalNum 总行数
     * @return
     */
    @GetMapping("/exportTable")
    public Result exportTable(@RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize,
                              @RequestParam("userCode") String userCode,
                              @RequestParam("userState") String userState,
                              @RequestParam("userType") String userType,
                              @RequestParam("totalNum") Integer totalNum
                              ){

        Page page = Page.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalNum(totalNum)
                .build();
        User user = User.builder()
                .userCode(userCode)
                .userState(userState)
                .userType(userType)
                .build();
        return userService.exportUserData(page, user);
    }
}
