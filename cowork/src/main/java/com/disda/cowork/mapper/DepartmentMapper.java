package com.disda.cowork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disda.cowork.po.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author disda
 * @since 2022-12-09
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartments(int parentId);

    void addDepartment(Department dep);

    void insertAndGetId(Department dep);
}
