package com.disda.cowork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disda.cowork.po.MenuRole;
import com.disda.cowork.dto.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author disda
 * @since 2022-01-26
 */
public interface IMenuRoleService extends IService<MenuRole> {

    boolean updateMenuRole(Integer rid, Integer[] mids);
}
