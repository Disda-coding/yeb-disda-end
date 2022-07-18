package com.disda.cowork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disda.cowork.po.MenuRole;
import com.disda.cowork.vo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author disda
 * @since 2022-01-26
 */
public interface IMenuRoleService extends IService<MenuRole> {

    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
