package com.disda.cowork.service;

import com.disda.cowork.dto.RespBean;

public interface ISaltService {
    RespBean generateSalt(String username);

}
