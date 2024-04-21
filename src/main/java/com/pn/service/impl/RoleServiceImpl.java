package com.pn.service.impl;

import com.github.pagehelper.PageHelper;
import com.pn.dto.AuthDTO;

import com.pn.dto.ExportDTO;
import com.pn.entity.Auth;
import com.pn.entity.Result;
import com.pn.entity.Role;
import com.pn.entity.User;
import com.pn.mapper.AuthMapper;
import com.pn.mapper.RoleMapper;
import com.pn.page.Page;
import com.pn.service.RoleService;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @version 1.0
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    //注入redis模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Role> queryAllRole() {
        List<Role> roleList = roleMapper.queryAllRole();
        return roleList;
    }


    @Override
    public Page queryRolePage(Page page, Role role) {
        //查询用户的总行数
        int roleCount = roleMapper.selectRoleCount(role);
        //分页查询用户的账号
        List<Role> roleList = roleMapper.selectRolePage(page,role);
        //将查询到的总行数和当前页数据组装到Page对象中
        page.setTotalNum(roleCount);
        page.setResultList(roleList);
        return page;
    }

    @Override
    public Result updateRoleState(Role role) {
        int i = roleMapper.updateRoleState(role);
        if (i > 0){
            return Result.ok("角色状态修改成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"角色状态修改失败!");
    }

    @Override
    public Result saveRole(Role role) {
       Role oldRole = roleMapper.findRoleByName(role);
       if (oldRole != null){
           return Result.err(Result.CODE_ERR_BUSINESS,"角色名称已存在!");
       }
       //添加
       int i = roleMapper.insertRole(role);
       if (i > 0){
           return Result.ok("角色添加成功!");
       }
       return Result.err(Result.CODE_ERR_BUSINESS,"角色添加失败!");
    }

    @Override
    @Transactional //事务
    public Result removerRoleByIds(List<Integer> roleIdList) {
        //删除角色和权限的关系
        roleMapper.deleteRoleAuthByRoleIds(roleIdList);

        //删除角色和用户的关系
        roleMapper.deleteRoleUserByRoleIds(roleIdList);

        //查询是否有不可以删除的角色
        int notDeleteCount = roleMapper.selectNotDeleteCount(roleIdList);

        if (notDeleteCount > 0){
            return Result.err(Result.CODE_ERR_BUSINESS,"角色删除失败,有"+notDeleteCount+"个角色启动中!");
        }
        //删除角色
        int i =roleMapper.deleteByRoleIds(roleIdList);
        if (i > 0){
            return Result.ok("角色删除成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"角色删除失败!");
    }
    //注入AuthMapper //TODO 逻辑错误
    @Autowired
    private AuthMapper authMapper;
    @Override
    public List<Auth> findAuthTree(Integer roleId) {
//        List<Auth> allAuthList = roleMapper.findAuthTree(roleId);
        //TODO 逻辑错误
        List<Auth> allAuthList = authMapper.allAuthTree();
        Map<Integer, Auth> permissionMap = new HashMap<>();
        //将所有权限菜单List<Auth>给转换成权限菜单树Map<Auth>
        allAuthList.forEach(auth -> {
            permissionMap.put(auth.getAuthId(), auth);
        });
        //将所有的子权限添加到对应的父权限中
        for(Auth auth : allAuthList){
            if(auth.getParentId() != 0){
                permissionMap.get(auth.getParentId()).getChildAuth().add(auth);
            }
        }

        List<Auth> authList= new ArrayList<>();
        for(Auth auth : allAuthList){
            if(auth.getParentId() == 0){
                authList.add(auth);
            }
        }
        return authList;
    }

    @Override
    @Transactional
    public Result saveRoleAuth(AuthDTO authDTO,Integer userId) {
        //先删除角色和权限的关系
        roleMapper.deleteRoleAuthByRoleIds(Arrays.asList(authDTO.getRoleId()));

        //添加角色和权限的关系
        int i = roleMapper.insertRoleAuth(authDTO);
        if (i > 0){
            //删除redis缓存,避免缓存数据与数据库数据不一致
            stringRedisTemplate.delete(userId + ":authTree");
            return Result.ok("角色权限分配成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"角色权限分配失败!");
    }

    @Override

    public void exportData(Page page, Role role, HttpServletResponse response) {
        // 查询数据
        Page rolePage = this.queryRolePage(page, role);

        // 获取Excel模板输入流
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/warehouse.xlsx");
        if (in == null) {
            throw new RuntimeException("Excel模板文件未找到");
        }

        try (
             XSSFWorkbook excel = new XSSFWorkbook(in);
             ServletOutputStream outputStream = response.getOutputStream()) {

            // 设置响应头以便浏览器识别为下载文件
            //设置响应的内容类型（Content-Type），指明服务器发送回客户端的数据格式。
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //设置响应头，告诉浏览器以附件的形式下载
            response.setHeader("Content-Disposition", "attachment;filename=roles.xlsx");

            // 获取sheet页
            XSSFSheet sheet = excel.getSheetAt(0);

            // 填充时间
            sheet.createRow(0).createCell(0).setCellValue(new Date());

            // 填充角色数据
            int rowIndex = 1; // 从第二行开始填充数据
            for (Role r : (List<Role>) rolePage.getResultList()) {
                XSSFRow row = sheet.getRow(rowIndex); // 获取或创建行
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }

                row.createCell(0).setCellValue(r.getRoleId());
                row.createCell(1).setCellValue(r.getRoleName());
                row.createCell(2).setCellValue(r.getRoleDesc());
                row.createCell(3).setCellValue(r.getRoleCode());
                row.createCell(4).setCellValue(r.getRoleState());
                row.createCell(5).setCellValue(r.getCreateTime());
                row.createCell(6).setCellValue(r.getUpdateTime());
                row.createCell(7).setCellValue(r.getCreateBy());
                row.createCell(8).setCellValue(r.getUpdateBy());

                rowIndex++;
            }

            // 写入输出流并关闭
            excel.write(outputStream);

        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }

    }

}
