package com.disda.cowork.dto;

import com.disda.cowork.po.Role;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * @program: cowork-back
 * @description: 管理员注册
 * @author: Disda
 * @create: 2022-12-27 11:10
 */
@Data
@ToString
@Accessors(chain = true)
public class AdminLogonParam  {

    @NotBlank(message = "管理员名字不能为空")
    private String name;
    private String phone;
    @Pattern(regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$",message = "请输入正确手机号")
    private String telephone;
    private String address;
    @NotBlank(message = "用户名不能为空")
    @Length(min = 6,max = 30,message = "用户名长度必须在6到30之间")
    @Pattern(regexp = "[a-zA-Z0-9_]+",message = "用户名只能为英文大小写和下划线")
    private String username;
    private String userFace;
    @Length(min = 6,message = "用户名密码最少长度为6")
    private String password;
    private String remark;
    private List<Role> roles;
}