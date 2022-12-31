package com.disda.cowork.controller;

import com.disda.cowork.dto.RespBean;
import com.disda.cowork.error.BusinessException;
import com.disda.cowork.po.Department;
import com.disda.cowork.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: cowork-back
 * @description: 部门管理
 * @author: Disda
 * @create: 2022-12-09 18:53
 */
@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/")
    public RespBean addDepartment(@RequestBody Department dep){
        Department department = departmentService.addDepartment(dep);
        return RespBean.success("成功添加部门",department);
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("/{id}")
    public RespBean deleteDepartment(@PathVariable Integer id) throws BusinessException {
         if(departmentService.deleteDepartment(id)){
             return RespBean.success("删除成功！");
         }
         return RespBean.success("删除失败！");
    }


}