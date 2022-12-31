package com.disda.cowork.dto;/**
 * @Author disda
 * @Date 2022/1/24
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: cowork-back
 * @description: 用户登录实体类
 * 链式加载 该注解设置为chain=true，生成setter方法返回this（也就是返回的是对象），代替了默认的返回void。
 * @author: Disda
 * @create: 2022-01-24 10:08
 */
@Data
//在JavaBean或类JavaBean中使用，使用此注解会自动重写对应的equals方法和hashCode方法；
// callSuper=false 只比较子类的属性，也就是讲：如果两个对象子类属性一致，父类属性不一致，在比较时候出现相同的结果，也就是返回的true。
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AdminLogin对象",description = "用于接受前端传输的认证信息")
public class AdminLoginParam {
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @ApiModelProperty(value = "密码",required = true)
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;
}