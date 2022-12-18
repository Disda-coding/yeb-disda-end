package com.disda.cowork.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Disda
 * @since 2022-10-12
 */

/**
 * 导入Excel文件时，名字，政治面貌等应传入id到数据库
 *
 * 修改@EqualsAndHashCode()，添加of = "name" 可通过参数of指定仅使用哪些属性
 *
 * 在类上添加@NoArgsConstructor，@RequiredArgsConstructor，
 * 详解https://blog.csdn.net/weixin_41540822/article/details/86606513
 *
 * 在类里加@NonNull,
 * ,@RequiredArgsConstructor也是在类上使用，但是这个注解可以生成带参或者不带参的构造方法。
 * 若带参数，只能是类中所有带有 @NonNull注解的和以final修饰的未经初始化的字段
 */
@Data
// 无参构造，有有参就要有无参
@NoArgsConstructor
// name必填的有参构造方法
@RequiredArgsConstructor
// 重写hashcode方法，使用name作为唯一标识
@EqualsAndHashCode(callSuper = false,of = "name")
@TableName("t_nation")
@ApiModel(value="Nation对象", description="")
public class Nation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "民族")
    @Excel(name = "民族")
    @NonNull
    private String name;


}
