package com.disda.cowork.controller;


import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.Admin;
import com.disda.cowork.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 在线聊天
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/admin")
    public RespBean getAllAdmins(String keywords){
        return RespBean.success(adminService.getAllAdmins(keywords));
    }

}
