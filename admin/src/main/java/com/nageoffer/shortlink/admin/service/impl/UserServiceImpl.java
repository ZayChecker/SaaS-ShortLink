package com.nageoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.common.convention.exception.ClientException;
import com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dao.mapper.UserMapper;
import com.nageoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.GroupService;
import com.nageoffer.shortlink.admin.service.UserService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.nageoffer.shortlink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final GroupService groupService;

    public UserRespDTO getUserByUsername(String username) {
        UserDO user = lambdaQuery()
                .eq(UserDO::getUsername, username)
                .one();
        if(user == null){
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        return BeanUtil.copyProperties(user, UserRespDTO.class);
    }

    @Override
    public Boolean hasUsername(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO userRegisterReqDTO) {
        if(hasUsername(userRegisterReqDTO.getUsername())){
            throw new ClientException(UserErrorCodeEnum.USER_EXIST);
        }
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + userRegisterReqDTO.getUsername());
        try {
            if(lock.tryLock()) {  //Lock()等待直到拿到锁
                UserDO userDO = BeanUtil.copyProperties(userRegisterReqDTO, UserDO.class);
                save(userDO);
                userRegisterCachePenetrationBloomFilter.add(userRegisterReqDTO.getUsername());
                //用户注册成功创建默认分组
            }
            else throw new ClientException(UserErrorCodeEnum.USER_EXIST);
        }finally {
            lock.unlock();
        }

    }

    @Override
    public void updateUser(UserUpdateReqDTO userUpdateReqDTO) {
        //验证当前用户是否为登录用户
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, userUpdateReqDTO.getUsername());
        baseMapper.update(BeanUtil.toBean(userUpdateReqDTO, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO) {
        UserDO userDO = lambdaQuery()
                .eq(UserDO::getPassword, userLoginReqDTO.getPassword())
                .eq(UserDO::getUsername, userLoginReqDTO.getUsername())
                .eq(UserDO::getDelFlag, 0)    //未注销
                .one();
        if(userDO == null){
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }

        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + userLoginReqDTO.getUsername());
        if(hasLogin){
            throw new ClientException("用户已登录");
        }

        String uuid = UUID.randomUUID().toString();
        //防止用户重复登录，hash结构
        stringRedisTemplate.opsForHash().put("login_" + userLoginReqDTO.getUsername(), uuid, JSONUtil.toJsonStr(userDO));
        stringRedisTemplate.expire("login_" +userLoginReqDTO.getUsername(), 30L, TimeUnit.MINUTES);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public void logout(String username, String token) {
        if(stringRedisTemplate.opsForHash().get("login_" + username, token) != null){
            stringRedisTemplate.delete("login_" + username);
        }
        else throw new ClientException("用户未登录或用户token不存在");
    }
}
