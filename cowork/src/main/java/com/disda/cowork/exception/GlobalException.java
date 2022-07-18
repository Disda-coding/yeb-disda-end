package com.disda.cowork.exception;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.disda.cowork.pojo.RespBean;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e){
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return RespBean.error("该数据有关联数据，操作失败");
        }
        return RespBean.error("数据库异常，操作失败");
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, HttpServletResponse response, Exception ex){
        Map<String,Object> responseData=new HashMap();
        if(ex instanceof BusinessException){
            BusinessException businessException=(BusinessException)ex;
            responseData.put("code", businessException.getErrCode());
            responseData.put("message",businessException.getErrMsg());
        }else if(ex instanceof ServletRequestBindingException){
            ex.printStackTrace();
            responseData.put("code", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("message","url绑定路由问题");
        }else if (ex instanceof NoHandlerFoundException){
            responseData.put("code", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("message", "没有找到对应的访问路径");
        }else{
            ex.printStackTrace();
            responseData.put("code", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("message",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }

        return RespBean.error(responseData);

    }

}
