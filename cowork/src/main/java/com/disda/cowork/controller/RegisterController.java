package com.disda.cowork.controller;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.disda.cowork.pojo.Admin;
import com.disda.cowork.pojo.RespBean;
import com.disda.cowork.service.ISendMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
public class RegisterController {

    @Autowired
    ISendMailService sendMailService;
    @PostMapping("/register")
    public RespBean Register(@RequestBody Admin admin) throws BusinessException {
        throw new BusinessException(EmBusinessError.MAIL_NOT_EXIST);
//        return null;

    }
}