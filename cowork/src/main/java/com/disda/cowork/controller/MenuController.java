package com.disda.cowork.controller;


import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.Menu;
import com.disda.cowork.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author disda
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "通过用户id查询菜单列表")
    @GetMapping("/menu")
    public RespBean getMenusByAdminId(){
        return RespBean.success(menuService.getMenusByAdminId());
    }


}
