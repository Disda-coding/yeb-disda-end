package com.disda.cowork.dto;
/**
 * @Author disda
 * @Date 2022/1/10
 */



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object data;

    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200,message,null);
    }

    public static RespBean success(){
        return success(null);
    }

    public static RespBean success(Object o){
        return new RespBean(200,"",o);
    }

    /**
     * 成功返回结果
     * @param message
     * @param data
     * @return
     */
    public static RespBean success(String message,Object data){
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

    /**
     * 失败返回结果
     * @param message
     * @param data
     * @return
     */
    public static RespBean error(String message,Object data){
        return new RespBean(500,message,data);
    }

    public static RespBean error(Map<String,Object> respMap){
        return new RespBean((Long)respMap.get("code"),(String)respMap.get("message"),null);
    }

}