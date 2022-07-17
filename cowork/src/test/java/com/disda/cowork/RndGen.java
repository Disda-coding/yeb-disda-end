package com.disda.cowork;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: cowork-back
 * @description:
 * @author: Disda
 * @create: 2022-07-16 21:54
 */
@SpringBootTest
public class RndGen {
    @Test
    public void gen(){
        for (int i=0;i<1000;i++)
        System.out.println((int)((Math.random()*9+1)*100000));
    }
}