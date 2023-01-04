package com.disda.cowork.error;

public enum EmBusinessError implements CommonError{
    //通用错误类型10001  000开头默认前面0都没了
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),
    DB_INSERT_ERROR(10003,"数据库插入错误"),
    SERVER_ERROR(10004,"服务器内部错误，请联系管理员"),

    //20000开头为用户信息相关错误定义
    MAIL_NOT_EXIST(20005,"用户邮箱不存在或不支持"),
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOCKED(20002,"用户被锁定，请联系管理员"),
    USER_LOGIN_FAIL(20003,"用户名或密码不正确"),
    CAPTCHA_ERROR(20004,"验证码错误"),
    USER_NOT_LOGIN(20005,"用户还未登陆"),
    USER_NOT_PERMITTED(20006,"用户权限不够"),
    USERNAME_EXIST(20007,"注册用户已存在！"),

    //30000开头的为业务错误
    STOCK_NOT_ENOUGH(30001,"未找到资源"),

    //40000开头的为非法请求
    ILLEGAL_PARAMETERS(40000,"非法参数请求"),
    TOO_MANY_REQUESTS(40001,"非法多次请求")
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
    public void setErrCode(long errCode) {
        this.errCode = errCode;
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
