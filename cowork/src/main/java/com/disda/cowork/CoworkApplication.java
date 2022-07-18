package com.disda.cowork;


import net.minidev.json.JSONUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.disda.cowork.mapper")
public class CoworkApplication {
    //http://localhost:8081/doc.html

    public static void main(String[] args) {
        SpringApplication.run(CoworkApplication.class, args);
    }

}
