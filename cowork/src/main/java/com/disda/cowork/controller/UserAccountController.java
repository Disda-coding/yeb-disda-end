package com.disda.cowork.controller;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.disda.cowork.po.Admin;
import com.disda.cowork.vo.AdminRegisterParam;
import com.disda.cowork.vo.RespBean;
import com.disda.cowork.service.ISendMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-17 19:28
 */
@RestController
@Slf4j
public class UserAccountController {

    @Autowired
    ISendMailService sendMailService;
    @PostMapping("/register")
    public RespBean Register(@Validated @RequestBody AdminRegisterParam adminRegisterParam) throws BusinessException {
        // 验证用户信息是否合法(使用Validated),验证用户名是否存在（默认允许一个邮箱多个用户）
        // 发送邮箱验证码

        return null;
    }
    @PostMapping("/validater")
    public RespBean ValidateRegCode(@Validated @RequestBody AdminRegisterParam adminRegisterParam){
        // 验证用户名是否在redis中
        // 验证用户的验证码是否和服务器一致
        // 将用户信息入库
        return null;
    }
    @PostMapping("/retrieveAccount")
    public RespBean retrieveAccount(@Validated @RequestBody AdminRegisterParam adminRegisterParam){
        // 验证邮箱是否合法
        // 数据库查询邮箱是否存在
        // 发送邮件
        return null;
    }
    @PostMapping("/resetAccount")
    public RespBean resetAccount(@Validated @RequestBody AdminRegisterParam adminRegisterParam){
        // 验证邮箱是否合法
        //判断邮箱验证码是否正确
        //更改密码
        return null;
    }
}