package com.disda.cowork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disda.cowork.po.Role;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author disda
 * @since 2022-01-26
 */
public interface RoleMapper extends BaseMapper<Role> {
    // 根据用户id查找角色列表
    List<Role> getRoles(Integer adminId);
}
