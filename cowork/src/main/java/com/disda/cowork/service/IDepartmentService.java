package com.disda.cowork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disda.cowork.error.BusinessException;
import com.disda.cowork.po.Department;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author disda
 * @since 2022-12-09
 */
public interface IDepartmentService extends IService<Department> {

    List<Department> getAllDepartments();

    Department addDepartment(Department dep);

    boolean deleteDepartment(Integer id) throws BusinessException;
}
