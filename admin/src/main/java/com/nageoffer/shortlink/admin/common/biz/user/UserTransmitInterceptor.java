package com.nageoffer.shortlink.admin.common.biz.user;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserTransmitInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;

    public UserTransmitInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = request.getHeader("username");
        String token = request.getHeader("token");
        if(StrUtil.isBlank(username) || StrUtil.isBlank(token)){
            return false;
        }
        Object userInfoJsonStr = stringRedisTemplate.opsForHash().get("login_" + username, token);
        if (userInfoJsonStr != null) {
            UserInfoDTO userInfoDTO = JSONUtil.toBean(userInfoJsonStr.toString(), UserInfoDTO.class);
            UserContext.setUser(userInfoDTO);
            return true;
        }
        else return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
