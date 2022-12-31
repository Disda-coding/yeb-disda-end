package com.disda.cowork.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.disda.cowork.po.Menu;
import com.disda.cowork.po.MenuRole;
import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.Role;
import com.disda.cowork.service.IMenuRoleService;
import com.disda.cowork.service.IMenuService;
import com.disda.cowork.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/")
    public RespBean getAllRoles(){
        return RespBean.success(roleService.list());
    }

    @ApiOperation("添加角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role){
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if (roleService.save(role)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid){
        if (roleService.removeById(rid)){
           return RespBean.success("删除成功");
        }
        return RespBean.success("删除失败");
    }


    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public RespBean getAllMenus(){
        return RespBean.success(menuService.getAllMenus());
    }

    @ApiOperation(value = "根据角色id查找菜单id")
    @GetMapping("/mid/{rid}")
    public RespBean getMidByRid(@PathVariable Integer rid){
        return RespBean.success(menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid))
                .stream().map(MenuRole::getMid).collect(Collectors.toList()));
    }

    @ApiOperation(value = "根据角色id更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid,Integer[] mids){
        if(menuRoleService.updateMenuRole(rid,mids)) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");

    }

}
