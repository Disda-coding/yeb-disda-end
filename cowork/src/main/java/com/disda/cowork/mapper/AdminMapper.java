package com.disda.cowork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disda.cowork.po.Admin;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author disda
 * @since 2022-01-24
 */
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> getAllAdmins(Integer id, String keywords);
}
