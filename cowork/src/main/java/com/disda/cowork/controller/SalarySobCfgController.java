package com.disda.cowork.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.disda.cowork.dto.RespBean;
import com.disda.cowork.dto.RespPageBean;
import com.disda.cowork.po.Employee;
import com.disda.cowork.po.Salary;
import com.disda.cowork.service.IEmployeeService;
import com.disda.cowork.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工工资帐套
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {

    @Autowired
    private ISalaryService salaryService;

    @Autowired
    private IEmployeeService employeeService;


    @ApiOperation(value = "获取所有工资帐套")
    @GetMapping("/salaries")
    public RespBean getAllSalaries(){
        return RespBean.success(salaryService.list());
    }

    @ApiOperation(value = "获取所有员工及其工资帐套")
    @GetMapping("/")
    public RespBean getAllEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                                 @RequestParam(defaultValue = "10") Integer size){
        return RespBean.success(employeeService.getAllEmployeeWithSalary(currentPage,size));
    }

    @ApiOperation(value = "更新员工工资帐套")
    @PutMapping("/")
    public RespBean updateEmployeeSalary(Integer sid, Integer eid){
        if (employeeService.update(new UpdateWrapper<Employee>().set("salaryId",sid).eq("id",eid))){
            return RespBean.success("更新成功");
        };
        return RespBean.error("更新失败");
    }

}
