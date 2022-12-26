package com.disda.cowork;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-20 21:43
 */
@SpringBootTest(classes = CoworkApplication.class)

public class test {
    @Test
    public void test(){
        assert 0.0f == 0;
//        if (StringUtils.equals("111",new StringBuilder("111"))){
//            System.out.println(1);
//        }
    }
}