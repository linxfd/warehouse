package com.pn.service.impl;

import com.alibaba.fastjson.JSON;
import com.pn.entity.Auth;
import com.pn.mapper.AuthMapper;
import com.pn.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    //注入AuthMapper
    @Autowired
    private AuthMapper authMapper;

    //注入redis模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //根据用户的id来查询用户权限菜单树的业务方法
    @Override
    public List<Auth> findAuthTree(int userId) {
        //先从redis中取查询缓存，查询的时权限菜单树List<Auth>集合，然后在转换成json字符串
        String authTreeListJson = stringRedisTemplate.opsForValue().get(userId + ":authTree");
        //判断authTreeListJson中是否存在数据
        //在redis中查到了缓存的数据权限菜单树List<Auth>集合
        if(StringUtils.hasText(authTreeListJson)){
            //将json字符串转换回权限菜单树List<Auth>集合
            List<Auth> authTreeList = JSON.parseArray(authTreeListJson,Auth.class);
            return authTreeList;
        }
        //redis中没有查询到，从数据库中查询权限菜单树List<Auth>集合
        List<Auth> allAuthList = authMapper.findAllAuth(userId);
        //将所有权限菜单List<Auth>给转换成权限菜单树List<Auth> 定义父权限的值为pid=0
        //调用了其他的方法
        List<Auth> authTreeList = allAuthToAuthTree(allAuthList,0);
        //将权限菜单树List<Auth>给转换成json字符串，，并且保存到redis中
        stringRedisTemplate.opsForValue().set(userId + ":authTree",JSON.toJSONString(authTreeList));
        //响应
        return authTreeList;
    }

    /**
     * 将所有权限菜单给转换成权限菜单树的递归算法
     */
    private List<Auth> allAuthToAuthTree(List<Auth> allAuthList,int parentId){
        //获取父权限菜单id为参数parentId的所有权限菜单
        List<Auth> authList = new ArrayList<>();
        for(Auth auth:allAuthList){
            if(auth.getParentId() == parentId){
                //该集合存放一级菜单
                authList.add(auth);
            }
        }
        //查询List<Auth> authList中的每个权限菜单的所有子级权限菜单
        for(Auth auth:authList){
            //getAuthId是一级菜单的id，该集合中存放的都是二级菜单
            List<Auth> childAuthList = allAuthToAuthTree(allAuthList,auth.getAuthId());
            auth.setChildAuth(childAuthList);
        }
        //返回一级菜单的list集合
        return authList;
    }

}
