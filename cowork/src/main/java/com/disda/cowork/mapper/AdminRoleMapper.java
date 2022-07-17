package com.disda.cowork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disda.cowork.pojo.AdminRole;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author disda
 * @since 2022-01-24
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer addAdminRole(Integer adminId, Integer[] rids);
}
