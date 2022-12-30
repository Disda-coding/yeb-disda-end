package com.disda.cowork.controller;

import com.disda.cowork.dto.RespBean;
import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: cowork-back
 * @description: test
 * @author: Disda
 * @create: 2022-12-30 16:52
 */
@RestController("/test")
public class TestController {
    @GetMapping()
    @ApiOperation(value="获取盐")
    public RespBean hello() throws BusinessException {
        throw new BusinessException(EmBusinessError.USERNAME_EXIST);
    }
}