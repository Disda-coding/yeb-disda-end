package com.disda.cowork.controller;

import com.disda.cowork.pojo.AdminLoginParam;
import com.disda.cowork.pojo.RespBean;
import com.disda.cowork.service.ISaltService;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @program: cowork-back
 * @description: 用于生成盐
 * @author: Disda
 * @create: 2022-01-29 18:45
 */
@RestController
@Slf4j
public class SaltController {

    @Autowired
    ISaltService saltService;

    @ApiOperation(value="获取盐")
    @PostMapping("/getSalt")
    public RespBean getSalt(@RequestBody AdminLoginParam adminLoginParam){
//        System.out.println(adminLoginParam.getUsername());
        log.info("salt "+saltService.generateSalt(adminLoginParam.getUsername()));
        return saltService.generateSalt(adminLoginParam.getUsername());
    }
}