package com.disda.cowork.service;

public interface ISendMailService {
    public void sendMail(String reciever);
    public void sendRegMail(String MailAddr,String username,String verificationCode);

    boolean checkEmail(String email);
}
