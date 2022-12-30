package com.disda.cowork.dto;
/**
 * @Author disda
 * @Date 2022/1/10
 */



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

/**
 * 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean<T> {
    private long code;
    private String message;
    private T data;

    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200,message,null);
    }

    /**
     * 前端会拦截，因此我们返回200即可
     * @return
     */
    public static RespBean success(){
        return new RespBean(200,null,null);
    }

    public static <T> RespBean<T> success(T data){
        return new RespBean(200,"",data);
    }

    /**
     * 成功返回结果
     * @param message
     * @param data
     * @return
     */
    public static <T> RespBean<T> success(String message,T data){
        return new RespBean(200,message,data);
    }

    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static RespBean error(String message){
        return new RespBean(500,message,null);
    }

    public static RespBean error(long code,String message){
        return new RespBean(code,message,null);
    }
    /**
     * 失败返回结果
     * @param message
     * @param data
     * @return
     */
    public static <T> RespBean<T> error(String message,T data){
        return new RespBean(500,message,data);
    }



}