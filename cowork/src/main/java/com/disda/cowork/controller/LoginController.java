package com.disda.cowork.controller;/**
 * @Author disda
 * @Date 2022/1/24
 */

import com.disda.cowork.pojo.Admin;
import com.disda.cowork.vo.AdminLoginParam;
import com.disda.cowork.pojo.RespBean;
import com.disda.cowork.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * @program: cowork-back
 * @description: 登录控制器
 * @author: liu yan
 * @create: 2022-01-24 10:10
 */
@RestController
@Api(tags = "LoginController")
@Log
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录后返回token")
    @PostMapping("/loginC")
    public RespBean loginWithCaptcha(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request) throws Exception {
        // 开启验证码
        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),adminLoginParam.getCode(),request);
    }

    @ApiOperation(value = "无验证码登录模式，登录后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request) throws Exception {
        System.out.println(adminLoginParam.getPassword());
        // 无验证码模式
        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),request);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
//        log.info(principal.toString());
        if (principal==null){
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(HttpServletRequest request, HttpServletResponse response){

        return RespBean.success("注销成功！");
    }

}