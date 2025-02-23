package com.chen.service.impl;

import com.chen.dto.UserDTO;
import com.chen.entity.User;
import com.chen.enums.RespStatusEnum;
import com.chen.mapper.UserMapper;
import com.chen.service.UserService;
import com.chen.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultVO<String> login(UserDTO userDTO) {
        User user = userMapper.findByUsername(userDTO.getUsername());
        if (Objects.isNull(user)) {
            return ResultVO.error(RespStatusEnum.USER_NOT_FOUND);
        }
        if (!user.getStatus().equals(1)) {
            return ResultVO.error(RespStatusEnum.USER_ILLEGAL);
        }
        String password = DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes());
        if (!password.equals(user.getPassword())) {
            return ResultVO.error(RespStatusEnum.USER_PASSWORD_WRONG);
        }
        return ResultVO.success(user.getId().toString());
    }

    @Override
    public ResultVO<Void> createUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        List<User> repeatedUsers = userMapper.findByCreateInfo(user);
        if (Objects.nonNull(repeatedUsers) && !repeatedUsers.isEmpty()) {
            return ResultVO.error(RespStatusEnum.USER_CREATE_INFO_ILLEGAL);
        }
        user.setLevel(0);
        user.setCreatedTime(LocalDateTime.now());
        user.setStatus(1);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.insert(user);
        return ResultVO.success();
    }
}
