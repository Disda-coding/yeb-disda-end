package com.disda.cowork.service;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.vo.RespBean;

import javax.servlet.http.HttpServletRequest;

public interface IVerificationCodeService {
//    public boolean checkAddr(String addr);
    public RespBean verificationCodeGenerate(String username, String mailAddr, HttpServletRequest request) throws BusinessException;
}
