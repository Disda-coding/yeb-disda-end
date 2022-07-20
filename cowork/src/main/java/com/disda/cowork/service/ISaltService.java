package com.disda.cowork.service;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.vo.RespBean;

import javax.servlet.http.HttpServletRequest;

public interface ISaltService {
    RespBean generateSalt(String username);

}
