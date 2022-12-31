package com.disda.cowork.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.disda.cowork.dto.RespBean;
import com.disda.cowork.po.*;
import com.disda.cowork.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author disda
 * @since 2022-12-10
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPositionService positionService;
    @Autowired
    private IDepartmentService departmentService;

    @Deprecated
    @ApiOperation(value = "查询所有员工（分页）")
    @PostMapping("/advanceSearch")
    public RespBean getEmployeeAdvance(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestBody Employee employee,
            LocalDate[] beginDateScope) {
        return RespBean.success(employeeService.getEmployee(currentPage, size, employee, beginDateScope));
    }

    @ApiOperation(value = "查询所有员工(分页)")
    @GetMapping("/")
    public RespBean getEmployee(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer size,
             Employee employee,
            LocalDate[] beginDateScope) {
        return RespBean.success(employeeService.getEmployee(currentPage, size, employee, beginDateScope));
    }


    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politicsstatus")
    public RespBean getAllPoliticsStatus() {
        return RespBean.success(politicsStatusService.list());
    }

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/joblevels")
    public RespBean getAllJoblevels() {
        return RespBean.success(joblevelService.list());
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nations")
    public RespBean  getAllNations() {
        return RespBean.success(nationService.list());
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/positions")
    public RespBean getAllPositions() {
        return RespBean.success(positionService.list());
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/deps")
    public RespBean getAllDepartments() {
        return RespBean.success(departmentService.getAllDepartments());
    }

    @ApiOperation(value = "获取工号(最大)")
    @GetMapping("/maxWorkId")
    public RespBean maxWorkId() {
        return RespBean.success(employeeService.maxWorkId());
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee) {
        //需计算合同期限
        if (employeeService.addEmp(employee)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee) {
        //不考虑合同期限
        if (employeeService.updateById(employee)) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmp(@PathVariable Integer id) {
        if (employeeService.removeById(id)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    /**
     * produces：指定响应体返回类型和编码
     */
    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response) {
        //没有传入id，查所有
        List<Employee> list = employeeService.getAllEmployee(null);
        //导出参数，标题的名字，表的名字，Excel类型，HSSF 03版
        ExportParams params = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        //ExcelExportUtil，导出工具类
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, list);
        //输出流
        ServletOutputStream out = null;
        try {
            //以流形式输出
            response.setHeader("content-type", "application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode("员工表.xls", "utf-8"));

            out = response.getOutputStream();
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "导出模板")
    @PostMapping(value="/export", produces = "application/octet-stream")
    public RespBean exportTemplate(HttpServletResponse response) {
        List<Employee> list = new ArrayList<>();
        //导出参数，标题的名字，表的名字，Excel类型，HSSF 03版
        ExportParams params = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        //ExcelExportUtil，导出工具类
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class,list );
        //输出流
        ServletOutputStream out = null;
        try {
            //以流形式输出
            response.setHeader("content-type", "application/octet-stream");
            //防止中文乱码
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode("导出模板表.xls", "utf-8"));
            out = response.getOutputStream();
            workbook.write(out);
            return RespBean.success("导入成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return RespBean.error("导入失败！");
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @ApiOperation(value = "导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        //跳过标题行
        params.setTitleRows(1);

        //查询所有民族
        List<Nation> nationList = nationService.list();
        //查询所有政治面貌
        List<PoliticsStatus> politicsStatuses = politicsStatusService.list();
        //查询所有所属部门
        List<Department> departmentList = departmentService.list();
        //查询所有职称
        List<Joblevel> joblevelList = joblevelService.list();
        //查询所有职位
        List<Position> positionList = positionService.list();

        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);

            list.forEach(employee -> {
                //民族id

                //只有name的有参构造，只有name没有id
                Nation nation = new Nation(employee.getNation().getName());
                //通过查询出的所有名字中比对，获取下标
                int index = nationList.indexOf(nation);
                //通过查询出的下标，通过数据库中查询所有民族的nationList，
                //查出完整的nation，有id和name
                Nation fullNation = nationList.get(index);
                //通过完整的nation获取id，并设置到nationId中
                employee.setNationId(fullNation.getId());

                //政治面貌id(同理)
                Integer politicsStatusId = politicsStatuses.get(politicsStatuses.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId();
                employee.setPoliticId(politicsStatusId);

                //所属部门id
                Integer departmentId = departmentList.get(departmentList.indexOf(new Department(employee.getDepartment().getName()))).getId();
                employee.setDepartmentId(departmentId);

                //职称ID
                Integer joblevelId = joblevelList.get(joblevelList.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId();
                employee.setJobLevelId(joblevelId);

                //职位id
                Integer positionId = positionList.get(positionList.indexOf(new Position(employee.getPosition().getName()))).getId();
                employee.setPosId(positionId);
            });
            if (employeeService.saveBatch(list)){
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }


}

