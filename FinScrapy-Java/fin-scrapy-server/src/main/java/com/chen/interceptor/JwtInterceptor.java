package com.chen.interceptor;

import com.chen.constant.FinScrapyConstant;
import com.chen.context.UserContext;
import com.chen.properties.JwtProperties;
import com.chen.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String jwtToken = request.getHeader(jwtProperties.getHeader());
        try {
            Claims claims = JwtUtils.parseJwt(jwtToken, jwtProperties.getSecret());
            Long userId = Long.valueOf(claims.get(FinScrapyConstant.USER_ID_JWT_CLAIM_KEY).toString());
            UserContext.setUserId(userId);
            return true;
        } catch (Exception e) {
            response.sendError(401);
            return false;
        }

    }


}
