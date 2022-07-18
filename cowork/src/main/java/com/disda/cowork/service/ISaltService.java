package com.disda.cowork.service;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.pojo.RespBean;

public interface ISaltService {
    RespBean generateSalt(String username);
    RespBean verificationCodeGenerate(String username,String mailAddr) throws BusinessException;
}
