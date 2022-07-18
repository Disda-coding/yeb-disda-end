package com.disda.cowork.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * @program: cowork-back
 * @description: 用于用户注册
 * @author: Disda
 * @create: 2022-07-18 11:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterParam {

    private String telephone;

    @NotNull(message = "邮箱名不能为空")
    @javax.validation.constraints.Email(message = "邮箱格式不正确")
    private String Email;


    @NotNull(message = "姓名不能为空")
    private String name;

    @NotNull(message = "用户名不能为空")
    @Size(min=3, max=20, message="用户名长度只能在3-20之间")
    private String username;



}