package com.disda.cowork.service;

import com.disda.cowork.error.BusinessException;

public interface ISendMailService {
    public void sendMail(String reciever);
    public void sendRegMail(String MailAddr,String username,String verificationCode) throws BusinessException;

//    boolean checkEmail(String email);
}
