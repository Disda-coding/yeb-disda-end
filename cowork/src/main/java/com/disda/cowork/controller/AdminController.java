package com.disda.cowork.controller;


import com.disda.cowork.dto.AdminLogonParam;
import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.Admin;
import com.disda.cowork.service.IAdminService;
import com.disda.cowork.service.IRoleService;
import com.disda.cowork.service.converter.AdminParamConverter;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author disda
 * @since 2022-01-24
 */
@RestController
@Validated
@Slf4j
@RequestMapping("/system/admin")
public class AdminController {


    @Autowired
    private IAdminService adminService;

    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public RespBean getAllAdmins(String keywords) {
        return RespBean.success(adminService.getAllAdmins(keywords));
    }

    @ApiOperation(value = "更新操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin) {
        if (adminService.updateById(admin)) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id) {
        if (adminService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/")
    public RespBean addAdmin(@RequestBody @Valid AdminLogonParam adminLogonParam) {
        Admin admin = AdminParamConverter.INSTANCE.toConvertPO(adminLogonParam);
        if (adminService.insert(admin) == 1) {
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roles")
    public RespBean getAllRoles() {
        return RespBean.success(roleService.list());
    }

    @ApiOperation(value = "更新操作员角色")
    @PutMapping("/role")
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        if (adminService.updateAdminRole(adminId, rids)) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }


}
