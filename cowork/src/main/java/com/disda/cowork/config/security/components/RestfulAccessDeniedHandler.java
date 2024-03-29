package com.disda.cowork.config.security.components;


import com.disda.cowork.dto.RespBean;
import com.disda.cowork.error.EmBusinessError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**

 /**
 * @program: cowork-back
 * @description: 当访问接口没权限时，自定义返回结果
 * @author: disda
 * @create: 2022-01-24 15:35
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        PrintWriter out = httpServletResponse.getWriter();
        RespBean bean = RespBean.error(EmBusinessError.USER_NOT_PERMITTED.getErrCode(),EmBusinessError.USER_NOT_PERMITTED.getErrMsg());
        out.write(new ObjectMapper().writeValueAsString(bean));
        out.flush();
        out.close();
    }
}