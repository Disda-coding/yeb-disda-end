package com.disda.cowork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.disda.cowork.mapper.AdminMapper;
import com.disda.cowork.po.Admin;
import com.disda.cowork.service.IVerificationCodeService;
import com.disda.cowork.dto.RespBean;
import com.disda.cowork.service.ISaltService;
import com.disda.cowork.utils.SaltUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: cowork-back
 * @description: 生成盐
 * @author: Disda
 * @create: 2022-01-29 18:51
 */
@Service
@Slf4j
public class SaltServiceImpl implements ISaltService {

    @Autowired
    AdminMapper adminMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    @Qualifier("mail")
    IVerificationCodeService sendMailService;

    @Override
    public RespBean generateSalt(String username) {
        //根据用户名从数据库中获取用户信息
        QueryWrapper<Admin> userQueryWrapper = Wrappers.query();
        userQueryWrapper.eq("username", username);
        Admin admin = adminMapper.selectOne(userQueryWrapper);
        //如果userDetails为空 或 密码不匹配
        HashMap<String, String> res = new HashMap<>(1);
        if (admin == null) {
            return RespBean.error("用户名不存在！");
        }
        else {
            if (Boolean.TRUE.equals(redisTemplate.hasKey("salt_" + username))) {
                res.put("salt", (String) redisTemplate.opsForValue().get("salt_" + username));
            } else {
                String salt = SaltUtils.generateSalt(16);
                redisTemplate.opsForValue().set("salt_" + username, salt, 5, TimeUnit.MINUTES);
                res.put("salt", salt);
            }
            return RespBean.success(res);
        }
    }




}