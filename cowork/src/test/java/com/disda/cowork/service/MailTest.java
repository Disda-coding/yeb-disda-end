package com.disda.cowork.service;

import com.disda.cowork.service.ISendMailService;
import com.disda.cowork.service.impl.SendMailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-16 15:16
 */
@SpringBootTest
public class MailTest {
    @Autowired
    ISendMailService sendMailService;
    @Test
    public void sendMail(){
        sendMailService.sendRegMail("1","2","33");
    }
}