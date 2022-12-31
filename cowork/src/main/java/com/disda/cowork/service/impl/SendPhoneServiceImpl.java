package com.disda.cowork.service.impl;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.service.IVerificationCodeService;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: cowork-back
 * @description: 手机验证码
 * @author: Disda
 * @create: 2022-07-18 21:54
 */
public class SendPhoneServiceImpl implements IVerificationCodeService {


    public boolean checkAddr(String addr) {
        return false;
    }


    @Override
    public boolean verifyCode(String mailAddr, String code, String prefix) throws BusinessException {
        return false;
    }

    @Override
    public boolean verificationCodeGenerate(String username, String mailAddr, HttpServletRequest request, String prefix) throws BusinessException {
        return true;
    }
}