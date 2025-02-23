package com.chen.controller;

import com.chen.constant.FinScrapyConstant;
import com.chen.dto.UserDTO;
import com.chen.entity.User;
import com.chen.properties.JwtProperties;
import com.chen.service.UserService;
import com.chen.utils.JwtUtils;
import com.chen.vo.ResultVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/create")
    public ResultVO<Void> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping("/login")
    public ResultVO<String> login(@RequestBody UserDTO userDTO) {
        ResultVO<String> loginResult = userService.login(userDTO);
        if (Objects.isNull(loginResult.getData()) || loginResult.getData().isBlank()) {
            return ResultVO.error(loginResult.getCode(), loginResult.getMsg(), "");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put(FinScrapyConstant.USER_ID_JWT_CLAIM_KEY, loginResult.getData());
        String token = JwtUtils.createJwt(jwtProperties.getSecret(), claims, jwtProperties.getTtl());
        return ResultVO.success(token);
    }

}
