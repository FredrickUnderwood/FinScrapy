package com.chen.service;

import com.chen.dto.UserDTO;
import com.chen.entity.User;
import com.chen.vo.ResultVO;

public interface UserService {
    ResultVO<Void> createUser(UserDTO userDTO);

    ResultVO<String> login(UserDTO userDTO);
}
