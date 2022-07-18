package com.disda.cowork.exception;

import com.disda.cowork.error.BusinessException;
import com.disda.cowork.error.EmBusinessError;
import com.disda.cowork.vo.RespBean;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    // <1> 处理 form data方式调用接口校验失败抛出的异常
    @ExceptionHandler(BindException.class)
    public RespBean bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());
        return new RespBean(EmBusinessError.PARAMETER_VALIDATION_ERROR.getErrCode(),EmBusinessError.PARAMETER_VALIDATION_ERROR.getErrMsg(),collect);
    }
    // <2> 处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RespBean methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getDefaultMessage())
                .collect(Collectors.toList());
        return new RespBean(EmBusinessError.PARAMETER_VALIDATION_ERROR.getErrCode(),EmBusinessError.PARAMETER_VALIDATION_ERROR.getErrMsg(),collect);
    }
    // <3> 处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    public RespBean constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());
        return new RespBean(EmBusinessError.PARAMETER_VALIDATION_ERROR.getErrCode(),EmBusinessError.PARAMETER_VALIDATION_ERROR.getErrMsg(),collect);
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
