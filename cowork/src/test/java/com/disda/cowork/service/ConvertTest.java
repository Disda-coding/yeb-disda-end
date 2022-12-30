package com.disda.cowork.service;

import com.disda.cowork.dto.AdminLogonParam;
import com.disda.cowork.po.Admin;
import com.disda.cowork.service.converter.AdminParamConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @program: cowork-back
 * @description: 测试mapstruct
 * @author: Disda
 * @create: 2022-12-28 14:23
 */
@SpringBootTest
public class ConvertTest {

    @Test
    public void testConvert(){
        AdminLogonParam adminLogonParam = new AdminLogonParam();
        adminLogonParam.setName("haha");
        Admin carVo = AdminParamConverter.INSTANCE.toConvertPO(adminLogonParam);
        System.out.println(adminLogonParam);
        System.out.println(carVo);
    }
}