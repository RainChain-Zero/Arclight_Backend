package com.rainchain.arclight.utils;


import com.rainchain.arclight.entity.FrequencyWarning;
import com.rainchain.arclight.exception.OperationFailException;
import com.rainchain.arclight.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AdminMapper adminMapper;

    public void setKeyCache(String key) {

        redisTemplate.opsForList().rightPush("api_key", key);

    }

    public boolean hasKeyCache(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("api_key")) && redisTemplate.opsForList().indexOf("api_key", key) != null;

    }

    public void setBotQqCache(String qq) {
        redisTemplate.opsForList().rightPush("bot_qq", qq);
    }

    public boolean hasBotQqCache(String qq) {

        return Boolean.TRUE.equals(redisTemplate.hasKey("bot_qq")) && redisTemplate.opsForList().indexOf("bot_qq", qq) != null;
    }

    public boolean isFrequent(String key) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.opsForValue().set(key, "1", 1, TimeUnit.MINUTES);
        } else {
            Integer frequency = Integer.parseInt((String) Objects.requireNonNull(redisTemplate.opsForValue().get(key)));
            frequency = frequency + 1;
            if (frequency >= 21) {
                String qq = VerifyUtils.decodeQq(key);
                adminMapper.addFrequencyWarning(new FrequencyWarning(qq, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                throw new OperationFailException("请求频率过快，请稍后再试");
            }
            Long expireTime = redisTemplate.getExpire(key);
            if (null != expireTime && expireTime > 0) {
                redisTemplate.opsForValue().set(key, String.valueOf(frequency), expireTime, TimeUnit.SECONDS);
            }
        }
        return true;
    }

    public boolean isRegisterFrequent(String ipAddress) {
        if (redisTemplate.opsForHash().get("ip", ipAddress) == null) {
            redisTemplate.opsForHash().put("ip", ipAddress, 1);
        } else {
            redisTemplate.opsForHash().increment("ip", ipAddress, 1);
        }
        Object times = redisTemplate.opsForHash().get("ip", ipAddress);
        if (times != null && (Integer) times > 10) {
            throw new OperationFailException("同一ip下最多注册10个账号");
        }
        return true;
    }

    public String getVersion() {
        return (String) redisTemplate.opsForValue().get("version");
    }
}
