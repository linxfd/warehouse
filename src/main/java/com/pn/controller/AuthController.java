package com.pn.controller;

import com.pn.entity.Result;
import com.pn.service.AuthService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pn.entity.Auth;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限接口
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 查询所有权限
     * @return
     */
    @GetMapping("/auth-tree")
    public Result authList() {
        List<Auth> authList= authService.allAuthTree();
        return Result.ok(authList);
    }

}
