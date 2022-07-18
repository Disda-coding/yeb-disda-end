package com.disda.cowork.error;

public interface CommonError {
    public Long getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
