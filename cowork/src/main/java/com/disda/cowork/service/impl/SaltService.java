package com.disda.cowork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.disda.cowork.error.BusinessException;
import com.disda.cowork.mapper.AdminMapper;
import com.disda.cowork.po.Admin;
import com.disda.cowork.vo.RespBean;
import com.disda.cowork.service.ISaltService;
import com.disda.cowork.service.ISendMailService;
import com.disda.cowork.utils.SaltUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SaltService implements ISaltService {

    @Autowired
    AdminMapper adminMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ISendMailService sendMailService;

    @Override
    public RespBean generateSalt(String username) {
        //根据用户名从数据库中获取用户信息
        QueryWrapper<Admin> userQueryWrapper = Wrappers.query();
        userQueryWrapper.eq("username", username);
        Admin admin = adminMapper.selectOne(userQueryWrapper);
        //如果userDetails为空 或 密码不匹配
        HashMap<String, String> res = new HashMap<>();
        if (admin == null)
            return RespBean.error("用户名不存在！");
        else {
            if (Boolean.TRUE.equals(redisTemplate.hasKey("salt_" + username))) {
                res.put("salt", (String) redisTemplate.opsForValue().get("salt_" + username));
            } else {
                String salt = SaltUtils.generateSalt(16);
                redisTemplate.opsForValue().set("salt_" + username, salt, 5, TimeUnit.MINUTES);
                res.put("salt", salt);
            }
            return RespBean.returnObj(res);
        }
    }

    @Override
    public RespBean verificationCodeGenerate(String username,String mailAddr) throws BusinessException {
        if (Boolean.TRUE.equals(redisTemplate.hasKey("verificationCode_" + username))) {
            return RespBean.error("请勿重复请求邮箱验证码");
        }
        String verificationCode = generatorCode();
        redisTemplate.opsForValue().set("verificationCode_" + username,verificationCode, 5, TimeUnit.MINUTES);
        sendMailService.sendRegMail(mailAddr,username,verificationCode);
        return RespBean.success("已发送邮箱，请及时查看！");
    }

    public String generatorCode() {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(code);
    }
}