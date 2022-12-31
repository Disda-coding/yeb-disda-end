package com.disda.cowork.error;

public interface CommonError {
    public Long getErrCode();
    public void setErrCode(long errCode);
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
