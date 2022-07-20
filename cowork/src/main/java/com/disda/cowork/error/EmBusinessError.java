package com.disda.cowork.error;

public enum EmBusinessError implements CommonError{
    //通用错误类型10001  000开头默认前面0都没了
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),
    DB_INSERT_ERROR(10003,"数据库插入错误"),

    //20000开头为用户信息相关错误定义
    MAIL_NOT_EXIST(20005,"用户邮箱不存在或不支持"),
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确"),
    USER_NOT_LOGIN(20003,"用户还未登陆"),
    USER_NOT_PERMITTED(20004,"用户权限不够"),
    USERNAME_EXIST(20006,"注册用户已存在！"),

    //30000开头的为业务错误
    STOCK_NOT_ENOUGH(30001,"未找到资源")
    ;

    private long errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode=errCode;
        this.errMsg=errMsg;
    }

    @Override
    public Long getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
