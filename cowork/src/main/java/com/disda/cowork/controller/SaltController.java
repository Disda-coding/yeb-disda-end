package com.disda.cowork.controller;

import com.disda.cowork.dto.AdminLoginParam;
import com.disda.cowork.dto.RespBean;
import com.disda.cowork.error.BusinessException;
import com.disda.cowork.service.ISaltService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public RespBean getSalt(@RequestBody AdminLoginParam adminLoginParam) throws BusinessException {

        return RespBean.success(saltService.generateSalt(adminLoginParam.getUsername()));
    }
}