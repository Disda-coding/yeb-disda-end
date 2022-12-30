package com.disda.cowork;

import com.disda.cowork.dto.AdminLogonParam;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-20 21:43
 */
@SpringBootTest()
public class test {
    @Value("${default.password}")
    String password;


    @Test
    public void paramTest(){
        System.out.println(password);
    }

    @Test
    public void validTest(){
        AdminLogonParam admin = new AdminLogonParam();
//        admin.setName("111");
//        System.out.println(admin);
//        ValidationResult validationResult = validator.validate(admin);
        valid(admin);

    }
    public void valid(@Validated AdminLogonParam adminLogonParam){

    }

}