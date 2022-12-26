package com.disda.cowork.service;

import com.disda.cowork.dto.RespBean;
import com.disda.cowork.dto.RespPageBean;
import com.disda.cowork.po.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author disda
 * @since 2022-12-10
 */
public interface IEmployeeService extends IService<Employee> {

    RespPageBean getEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    List<Employee> getAllEmployee(Integer id);

    RespBean maxWorkId();

    RespBean addEmp(Employee employee);
    /**
     * 获取所有员工工资帐套
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getAllEmployeeWithSalary(Integer currentPage, Integer size);

}
