package com.disda.cowork.service;

import com.disda.cowork.dto.RespBean;
import com.disda.cowork.error.BusinessException;

import java.util.Map;

public interface ISaltService {
    Map generateSalt(String username) throws BusinessException;

}
