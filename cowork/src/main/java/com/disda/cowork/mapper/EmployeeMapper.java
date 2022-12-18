package com.disda.cowork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.disda.cowork.po.Employee;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author disda
 * @since 2021-10-12
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 查询所有员工(分页)
     * @param page
     * @param employee
     * @param beginDateScope
     * @return
     */
    IPage<Employee> getEmployee(Page<Employee> page,
                                @Param("employee") Employee employee,
                                @Param("beginDateScope") LocalDate[] beginDateScope);


    /**
     * 查询员工
     * @param id
     * @return
     */
    List<Employee> getAllEmployee(Integer id);

    /**
     * 获取所有员工工资帐套(分页)
     * @param page
     */
    IPage<Employee> getAllEmployeeWithSalary(Page<Employee> page);
}
