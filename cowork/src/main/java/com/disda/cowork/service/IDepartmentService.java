package com.disda.cowork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disda.cowork.po.Department;
import com.disda.cowork.dto.RespBean;

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

    RespBean addDepartment(Department dep);
}
