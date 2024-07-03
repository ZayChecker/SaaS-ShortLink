package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1/user")
public class UserController {

    private final UserService userService;

    //根据用户名查询用户
    @GetMapping("/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
        UserRespDTO user = userService.getUserByUsername(username);
        return Results.success(user);
    }

    @GetMapping("/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        return Results.success(userService.hasUsername(username));
    }

    //新增用户
    @PostMapping
    public void register(@RequestBody UserRegisterReqDTO userRegisterReqDTO){
        userService.register(userRegisterReqDTO);
    }

    //根据用户名修改用户
    @PutMapping
    public void updateUser(@RequestBody UserUpdateReqDTO userUpdateReqDTO){
        userService.updateUser(userUpdateReqDTO);
    }

    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO userLoginReqDTO){
        return Results.success(userService.login(userLoginReqDTO));
    }

    @DeleteMapping("/logout")
    public void logout(@RequestParam("username") String username, @RequestParam("token") String token){
        userService.logout(username, token);
    }
}

/**
 * Q：多少数据量分表？
 * 字段多或者有text字段，像垂直分表，主表里都是不会有那种大的text字段的，都是放扩展表
 * 都是小字段没必要分
 * Q：什么情况下分库？
 * 操作数据库的QPS，TPS越来越大的情况下要分库，数据库的链接有一个固定数量，连接会不够用
 */
