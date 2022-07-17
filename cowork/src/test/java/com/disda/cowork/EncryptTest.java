package com.disda.cowork;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-16 14:47
 */
@SpringBootTest
public class EncryptTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    public void test1(){
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pwd = passwordEncoder.encode("123");
        System.out.println(pwd);

    }
    @Test
    public void test2(){
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Boolean pwd = passwordEncoder.matches("123","$2a$10$ogvUqZZAxrBwrmVI/e7.SuFYyx8my8d.9zJ6bs9lPKWvbD9eefyCe");
        System.out.println(pwd);

    }

}