package com.disda.cowork.service;

import com.disda.cowork.po.Admin;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.concurrent.TimeUnit;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2023-01-03 15:27
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IAdminService adminServiceImpl;

    @Test
    public void test() throws JsonProcessingException {
        // UserDetails userDetails = userDetailService.loadUserByUsername("admin");
        Admin admin = adminServiceImpl.getAdminByUserName("admin");
        admin.setRoles(adminServiceImpl.getRoles(admin.getId()));

        redisTemplate.opsForValue().set("test",admin ,999999, TimeUnit.SECONDS);
        UserDetails user = (Admin) redisTemplate.opsForValue().get("test");
        System.out.println(user.getAuthorities());
    }
}