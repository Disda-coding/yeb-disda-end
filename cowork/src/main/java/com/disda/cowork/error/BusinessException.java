package com.disda.cowork.error;

//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError{
    private CommonError commonError;

    //直接接收EmBusinessError的传参用于构造业务异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }
    public BusinessException(long errCode,String errMsg){
        super();
        this.commonError.setErrMsg(errMsg);
        this.commonError.setErrCode(errCode);
    }

    //接收自定义errMsg的方式构造业务异常
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError=commonError;
        this.commonError.setErrMsg(errMsg);
    }




    @Override
    public Long getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public void setErrCode(long errCode) {
        this.commonError.setErrCode(errCode);
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        return this.commonError.setErrMsg(errMsg);
    }
}
