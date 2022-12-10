package com.disda.cowork.service;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.dto.RespBean;

import javax.servlet.http.HttpServletRequest;

public interface IVerificationCodeService {
    public boolean verifyCode(String mailAddr,String code,String prefix) throws BusinessException;
    public RespBean verificationCodeGenerate(String username, String mailAddr, HttpServletRequest request,String prefix) throws BusinessException;
}
