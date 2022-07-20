package com.disda.cowork.controller;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.disda.cowork.service.IAdminService;
import com.disda.cowork.service.IVerificationCodeService;
import com.disda.cowork.vo.AdminRegisterParam;
import com.disda.cowork.vo.RespBean;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-17 19:28
 */
@RestController
@RequestMapping("/register")
@Slf4j
public class UserAccountController {

    @Autowired
    @Qualifier("mail")
    IVerificationCodeService sendMailService;
    @Autowired
    IAdminService adminService;

    @ApiOperation("判断用户名是否合法")
    @GetMapping
    public RespBean Register(String username) throws BusinessException {
        System.out.println(username);
        if(adminService.getExistUserByUserName(username)){
            throw new BusinessException(EmBusinessError.USERNAME_EXIST);
        }
        //可以添加其他判断逻辑

        return RespBean.success("用户名可用！");
    }
    @PostMapping("/sendCode")
    public RespBean sendVerificationCode(@RequestBody AdminRegisterParam adminRegisterParam, HttpServletRequest request) throws BusinessException {
        //isBlank等价于params == null || “”.equals(params) || params.trim() == “”
        if(StringUtils.isBlank(adminRegisterParam.getUsername())||StringUtils.isBlank(adminRegisterParam.getEmail())||adminService.getExistUserByUserName(adminRegisterParam.getUsername())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        return sendMailService.verificationCodeGenerate(adminRegisterParam.getUsername(),adminRegisterParam.getEmail(),request);
    }


    @PostMapping()
    public RespBean Register(@Validated @RequestBody AdminRegisterParam adminRegisterParam) throws BusinessException {
        // 验证用户信息是否合法(使用Validated),验证用户名是否存在（默认允许一个邮箱多个用户）
        // 发送邮箱验证码

        return null;
    }
//    @PostMapping("/validater")
//    public RespBean ValidateRegCode(@Validated @RequestBody AdminRegisterParam adminRegisterParam){
//        // 验证用户名是否在redis中
//        // 验证用户的验证码是否和服务器一致
//        // 将用户信息入库
//        return null;
//    }
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