package com.cyberlanting.qwen_rag.service.impl;


import com.cyberlanting.qwen_rag.common.context.BaseContext;
import com.cyberlanting.qwen_rag.common.exception.*;
import com.cyberlanting.qwen_rag.common.result.Result;
import com.cyberlanting.qwen_rag.mapper.UserMapper;
import com.cyberlanting.qwen_rag.pojo.dto.UserDTO;
import com.cyberlanting.qwen_rag.pojo.entity.User;
import com.cyberlanting.qwen_rag.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户注册
     * @param userDTO 用户信息
     */
    public void register(UserDTO userDTO) {
        // 1. 判断用户名和邮箱是否已经存在
        User userWithUsername = User.builder().username(userDTO.getUsername()).build();
        if (!userMapper.findUser(userWithUsername).isEmpty()) {
            throw new UsernameExistsException("用户名已存在");
        }
        User userWithEmail = User.builder().email(userDTO.getEmail()).build();
        if (!userMapper.findUser(userWithEmail).isEmpty()) {
            throw new EmailExistsException("该邮箱已注册");
        }

        // 2. 插入用户数据
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        // 通过md5算法对密码进行加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        String avatarUrl = "https://www.dummyimage.com/600x400/fff/000000&text=" + user.getUsername().charAt(0);
        user.setAvatarUrl(avatarUrl);
        userMapper.save(user);
    }

    /**
     * 用户登录
     * @param userDTO 用户账密
     * @return 用户信息
     */
    public User login(UserDTO userDTO) {
        User userForLogin = User.builder()
                .email(userDTO.getEmail())
                .build();
        List<User> users =  userMapper.findUser(userForLogin);
        if (users.isEmpty()) {
            throw new UserNotExistsException("用户不存在");
        }
        userForLogin = users.get(0);
        String password = DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes());
        log.info(password);
        if (!password.equals(userForLogin.getPassword())) {
            throw new PasswordErrorException("密码错误");
        }
//        if (users.size() > 1) {
//            throw new UnknownException("未知错误：存在多个用户");
//        }
        return userForLogin;
    }

    private Long getUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new NotLoginException("用户未登录");
        }
        return userId;
    }

    /**
     * 根据id获取用户信息
     * @param id 用户id
     * @return 用户信息
     */
    public User getUserById(Long id) {
//        User userWithId = User.builder().id(id).build();
//        return userMapper.findUser(userWithId).get(0);
        return userMapper.getUserById(id);
    }

    public void update(UserDTO userDTO) {
        Long userId = getUserId();
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(userId);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.update(user);
    }

    public User getUserByEmail(String email) {
        User userWithEmail = User.builder().email(email).build();
        List<User> users =  userMapper.findUser(userWithEmail);
        if (users == null || users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

}
