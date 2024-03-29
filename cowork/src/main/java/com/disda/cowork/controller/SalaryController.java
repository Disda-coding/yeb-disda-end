package com.disda.cowork.controller;


import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.Salary;
import com.disda.cowork.service.ISalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author disda
 * @since 2022-12-12
 */
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {
    @Autowired
    private ISalaryService salaryService;

    @ApiOperation(value = "获取所有工资账单")
    @GetMapping("/")
    public RespBean getAllSalaries(){
        return RespBean.success(salaryService.list());
    }

    @ApiOperation(value = "添加工资账单")
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary){
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)){
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }

    @ApiOperation(value = "删除工资账单")
    @DeleteMapping("/{id}")
    public RespBean deleteSalary(@PathVariable Integer id){
        if (salaryService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "更新工资账单")
    @PutMapping("/")
    public RespBean updateSalary(@RequestBody Salary salary){
        if (salaryService.updateById(salary)){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }
}
