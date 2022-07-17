package com.disda.cowork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disda.cowork.pojo.MenuRole;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author disda
 * @since 2022-01-26
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    Integer insertRecord(Integer rid, Integer[] mids);
}
