package com.rainchain.arclight.interceptors;

import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws OperationFailException, UnsupportedEncodingException {
        String api_key = request.getParameter("api_key");
        if (api_key == null) {
            throw new OperationFailException("api_key不能为空！");
        }
        //检验key
        if (!redisUtils.hasKeyCache(api_key)) {
            throw new OperationFailException("api_key不存在！");
        }
        return true;
    }
}
