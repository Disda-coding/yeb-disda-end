package com.disda.cowork.service.converter;

import com.disda.cowork.dto.AdminLogonParam;
import com.disda.cowork.po.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @program: cowork-back
 * @description: 用于DTO和PO之间转换的工具
 * @author: Disda
 * @create: 2022-12-28 14:15
 */
@Mapper
public interface AdminParamConverter {

    AdminParamConverter INSTANCE = Mappers.getMapper(AdminParamConverter.class);
    Admin toConvertPO(AdminLogonParam source);
}