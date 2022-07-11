package com.rainchain.arclight.interceptors;

import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.utils.IpUtils;
import com.rainchain.arclight.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RegisterInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws OperationFailException {
        String ipAddress = IpUtils.getIpAddr(request);
        if (ipAddress == null || ipAddress.equals("")) {
            return true;
        }
        return redisUtils.isRegisterFrequent(ipAddress);
    }
}
