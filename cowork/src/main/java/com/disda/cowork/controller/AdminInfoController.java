package com.disda.cowork.controller;


import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.Admin;
import com.disda.cowork.service.IAdminService;
import com.disda.cowork.utils.FastDFSUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

/**
 * 个人中心
 */
@RestController()
@RequestMapping("/admin")
public class AdminInfoController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication){
        if (adminService.updateById(admin)){
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin, null, authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/info")
    public RespBean getAdminInfo(Principal principal) {
        if (principal == null) {
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return RespBean.success(admin);
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String,Object> info){
        String oldPass = (String)info.get("oldPass");
        String pass = (String)info.get("pass");
        Integer adminId = (Integer)info.get("adminId");
        if(adminService.updateAdminPassword(oldPass,pass,adminId)){
            return RespBean.success("更新密码成功！");
        };
        return RespBean.error("原密码不正确！");
    }

    @ApiOperation(value = "更新用户头像")
    @PutMapping("/userface")
    private RespBean updateAdminUserFace(MultipartFile file,Integer adminId,Authentication authentication){
        String[] filePaths = FastDFSUtils.upload(file);
        String url = FastDFSUtils.getTrackerUrl()+filePaths[0]+"/"+filePaths[1];
        if (adminService.updateAdminUserFace(url,adminId,authentication)){
            return RespBean.success("更新头像成功");
        }
        return RespBean.success("更新头像失败");
    }

}
