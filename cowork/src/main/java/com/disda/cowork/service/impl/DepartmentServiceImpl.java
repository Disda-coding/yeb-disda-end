package com.disda.cowork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disda.cowork.mapper.DepartmentMapper;
import com.disda.cowork.po.Department;
import com.disda.cowork.service.IDepartmentService;
import com.disda.cowork.dto.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author disda
 * @since 2022-12-09
 */
@Service
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> getAllDepartments(){
        return departmentMapper.getAllDepartments(-1);
    }

    @Override
    public RespBean addDepartment(Department dep) {
        //通过存储过程实现
//        dep.setEnabled(true);
//        departmentMapper.addDepartment(dep);
//        if(dep.getResult() == 1){
//            return RespBean.success("添加成功",dep);
//        }
//        return RespBean.error("添加失败");
        dep.setEnabled(true);
        dep.setIsParent(false);
        //插入新的部门
        departmentMapper.insertAndGetId(dep);
        Integer id = dep.getId();
        //获取父节点信息
        log.info(String.valueOf(dep.getParentId()));
        Department fatherDepartment = departmentMapper.selectOne(new QueryWrapper<Department>().eq("id",dep.getParentId()));
        dep.setDepPath(fatherDepartment.getDepPath()+"."+id);
        fatherDepartment.setIsParent(true);
        departmentMapper.updateById(fatherDepartment);
        departmentMapper.updateById(dep);
        return RespBean.success("成功添加部门");
    }
}
