package com.disda.cowork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disda.cowork.po.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author disda
 * @since 2022-01-26
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过用户id查询菜单列表
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色查找菜单列表
     */
    List<Menu> getMenusWithRole();

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> getAllMenus();
}
