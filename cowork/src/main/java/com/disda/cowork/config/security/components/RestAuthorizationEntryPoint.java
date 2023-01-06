package com.disda.cowork.config.security.components;

import com.disda.cowork.dto.RespBean;
import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: cowork-back
 * @description: 当未登录或者token失效时，访问接口返回的自定义结果
 * @author: Disda
 * @create: 2022-01-24 15:24
 */
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 不能通过throw自定义报错来实现，因为这个方法是overwritten的，父类方法没有抛异常
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 设置编码方式为UTF-8
        httpServletResponse.setCharacterEncoding("UTF-8");
        // 内容为json
        httpServletResponse.setContentType("application/json");
        PrintWriter out = httpServletResponse.getWriter();
        RespBean bean = RespBean.error(EmBusinessError.USER_NOT_LOGIN.getErrCode(),EmBusinessError.USER_NOT_LOGIN.getErrMsg());
        out.write(new ObjectMapper().writeValueAsString(bean));
        out.flush();
        out.close();
    }
}