package com.disda.cowork;

import com.disda.cowork.config.security.components.JwtTokenUtil;
import com.disda.cowork.dto.AdminLogonParam;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Date;

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
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    public void jwt() {

        String token = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NzM0MjA0NDUsInN1YiI6ImFkbWluIiwiY3JlYXRlZCI6MTY3MjgxNTY0NTQ4OX0.Cdl1RKV0C7yOSzysPr-RyyijZSnt3UkGBJW-3fzcGjHorVYBuEcFYfliCurkFyhjCR5esw2zw-_NYT0tks7Bbg";
        System.out.println(jwtTokenUtil.getExpiredDateFormToken(token));
        System.out.println(new Date());
        System.out.println(jwtTokenUtil.canRefresh(token));
    }

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