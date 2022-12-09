package com.disda.cowork.controller;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.disda.cowork.service.IAdminService;
import com.disda.cowork.service.IVerificationCodeService;
import com.disda.cowork.vo.AdminRegisterParam;
import com.disda.cowork.vo.AdminRetrieveParam;
import com.disda.cowork.vo.RespBean;
import io.swagger.annotations.Api;
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
@Api(tags = "用于用户注册相关")
@Slf4j
public class UserAccountController {

    @Autowired
    @Qualifier("mail")
    IVerificationCodeService sendMailService;
    @Autowired
    IAdminService adminService;

    @ApiOperation("判断用户名是否合法")
    @GetMapping()
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
        return sendMailService.verificationCodeGenerate(adminRegisterParam.getUsername(),adminRegisterParam.getEmail(),request,"verificationCode_");
    }


    @PostMapping()
    public RespBean Register(@Validated @RequestBody AdminRegisterParam adminRegisterParam) throws BusinessException {
        // 验证用户信息是否合法(使用Validated),验证用户名是否存在（默认允许一个邮箱多个用户） @Validated做了
        // 验证邮箱验证码
        if(sendMailService.verifyCode(adminRegisterParam.getEmail(),adminRegisterParam.getRegisterCode(),"verificationCode_"))
            return RespBean.error("验证码不正确！");

        // 成功则写入数据库（先放着，后期可能实现会改变）
        return RespBean.success("注册成功！");
    }

    @PostMapping("/retrieveAccount")
    public RespBean retrieveAccount(@RequestBody AdminRetrieveParam adminRetrieveParam,HttpServletRequest request) throws BusinessException {
        //后期加上判断邮箱是否存在
        if(StringUtils.isBlank(adminRetrieveParam.getEmail())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        return sendMailService.verificationCodeGenerate(null,adminRetrieveParam.getEmail(),request,"retrieveCode_");
    }
    @PostMapping("/resetAccount")
    public RespBean resetAccount(@Validated @RequestBody AdminRetrieveParam adminRetrieveParam) throws BusinessException {
//        System.out.println(adminRetrieveParam);
        if(sendMailService.verifyCode(adminRetrieveParam.getEmail(),adminRetrieveParam.getCode(),"retrieveCode_"))
            return RespBean.error("验证码不正确！");
        // 成功则写入数据库（先放着，后期可能实现会改变）
        return RespBean.success("找回成功！");
    }
}