package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // ✅ Store Refresh Token in Redis
    public void storeRefreshToken(String userId, String refreshToken, long expiryInMinutes) {
        String key = "refreshToken:" + userId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMinutes(expiryInMinutes));
    }

    // ✅ Get Refresh Token from Redis
    public String getRefreshToken(String userId) {
        String key = "refreshToken:" + userId;
        return redisTemplate.opsForValue().get(key);
    }

    // ✅ Delete Refresh Token (Optional for Logout)
    public void deleteRefreshToken(String userId) {
        String key = "refreshToken:" + userId;
        redisTemplate.delete(key);
    }
}
