package com.balabala.service.impl;

import com.balabala.Constants;
import com.balabala.Utils;
import com.balabala.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String newVerificationCode(String phoneNumber) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String redisKey = String.format(Constants.REDIS_VERIFICATION_CODE, phoneNumber);
        String code = Utils.randomNumeric(6);
        operations.set(redisKey, code, 10, TimeUnit.MINUTES);

        // TODO 发送手机短信

        return code;
    }

    @Override
    public boolean verify(String phoneNumber, String code) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String redisKey = String.format(Constants.REDIS_VERIFICATION_CODE, phoneNumber);
        String codeInRedis = operations.get(redisKey);

        if (Objects.equals(code, codeInRedis)) {
            redisTemplate.delete(redisKey);
            return true;
        }

        return false;
    }

}
