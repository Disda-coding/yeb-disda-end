package com.disda.cowork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disda.cowork.po.AdminRole;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author disda
 * @since 2022-01-24
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    Integer updateAdminRole(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);

}
