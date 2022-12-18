package com.disda.cowork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disda.cowork.mapper.DepartmentMapper;
import com.disda.cowork.mapper.EmployeeMapper;
import com.disda.cowork.po.Department;
import com.disda.cowork.po.Employee;
import com.disda.cowork.service.IDepartmentService;
import com.disda.cowork.dto.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private EmployeeMapper employeeMapper;

    public List<Department> getAllDepartments(){
        return departmentMapper.getAllDepartments(-1);
    }

    @Override
    @Transactional
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
        Department fatherDepartment = departmentMapper.selectOne(new LambdaQueryWrapper<Department>().eq(Department::getId,dep.getParentId()));
        dep.setDepPath(fatherDepartment.getDepPath()+"."+id);
        fatherDepartment.setIsParent(true);
        departmentMapper.updateById(fatherDepartment);
        departmentMapper.updateById(dep);
        return RespBean.success("成功添加部门");
    }

    @Override
    @Transactional
    public RespBean deleteDepartment(Integer id) {
        // 使用存储过程
//        Department dep = new Department();
//        dep.setId(id);
//        departmentMapper.deleteDep(dep);
//        if(dep.getResult() == -2){
//            return RespBean.error("该部门下还有子部门，删除失败！");
//        }else if(dep.getResult() == -1){
//            return RespBean.error("该部门下还有员工，删除失败！");
//        }else if (dep.getResult() == 1){
//            return RespBean.succuss("删除成功！");
//        }else{
//            return RespBean.error("删除失败！");
//        }
        // 如果删除的节点有子节点
        Integer resCount = departmentMapper.selectCount(new LambdaQueryWrapper<Department>().eq(Department::getId,id).eq(Department::getIsParent,false));
        if (resCount == 0) return RespBean.error("该部门下还有子部门，删除失败！");
        // 如果要删除的节点关联了职工表
        Integer empCount = employeeMapper.selectCount(new LambdaQueryWrapper<Employee>().eq(Employee::getDepartmentId,id));
        if(empCount>0) return RespBean.error("该部门下还有员工，删除失败！");
        //根据id查找
        Department dep = departmentMapper.selectById(id);
        Integer parentId = dep.getParentId();
        departmentMapper.delete(new LambdaQueryWrapper<Department>().eq(Department::getId,id).eq(Department::getIsParent,false));
        Integer pCount = departmentMapper.selectCount(new LambdaQueryWrapper<Department>().eq(Department::getParentId,parentId));
        if (pCount == 0)
            departmentMapper.update(null,new LambdaUpdateWrapper<Department>().eq(Department::getId,parentId).set(Department::getIsParent,false));
        return RespBean.success("删除成功！");
    }
}
