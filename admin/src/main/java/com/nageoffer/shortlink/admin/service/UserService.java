package com.nageoffer.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {

    UserRespDTO getUserByUsername(String username);

    //查询用户名是否存在
    Boolean hasUsername(String username);

    void register(UserRegisterReqDTO userRegisterReqDTO);

    void updateUser(UserUpdateReqDTO userUpdateReqDTO);

    UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO);

    void logout(String username, String token);
}
