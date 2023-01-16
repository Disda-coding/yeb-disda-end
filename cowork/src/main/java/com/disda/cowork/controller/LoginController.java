package com.disda.cowork.controller;/**
 * @Author disda
 * @Date 2022/1/24
 */

import com.disda.cowork.dto.AdminLoginParam;
import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.Admin;
import com.disda.cowork.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: cowork-back
 * @description: 登录控制器
 * @author: Disda
 * @create: 2022-01-24 10:10
 */
@RestController
@Api(tags = "登录控制器")
@Slf4j
public class LoginController {

    @Autowired
    private IAdminService adminService;


    @ApiOperation(value = "登录后返回token")
    @PostMapping("/loginC")
    public RespBean loginWithCaptcha(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request) throws Exception {
        // 开启验证码
        Map<String, String> tokenMap = adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword(), adminLoginParam.getCode(), request);
        if(tokenMap.containsKey("token")){
            return RespBean.success("登录成功！",tokenMap);
        }
        return RespBean.error("登录失败！");
    }

    @ApiOperation(value = "无验证码登录模式，登录后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request) throws Exception {
        // 无验证码模式
        Map<String,String> tokenMap =  adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword(), request);
        if(tokenMap.containsKey("token")){
            return RespBean.success("登录成功！",tokenMap);
        }
        return RespBean.error("登录失败！");
    }


    @PreAuthorize("hasAnyRole('ROLE_admin')")
    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(HttpServletRequest request, HttpServletResponse response) {

        return RespBean.success("注销成功！");
    }

}