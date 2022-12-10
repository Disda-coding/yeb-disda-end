package com.disda.cowork.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @program: cowork-back
 * @description: 取回账户
 * @author: Disda
 * @create: 2022-07-18 19:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRetrieveParam {
    @NotEmpty(message = "邮箱名不能为空")
    @javax.validation.constraints.Email(message = "邮箱格式不正确")
    private String Email;

    @ApiModelProperty(value = "密码",required = true)
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;
}