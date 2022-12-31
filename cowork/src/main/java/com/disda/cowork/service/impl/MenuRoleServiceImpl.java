package com.disda.cowork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disda.cowork.mapper.MenuRoleMapper;
import com.disda.cowork.po.MenuRole;
import com.disda.cowork.dto.RespBean;
import com.disda.cowork.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author disda
 * @since 2022-01-26
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {
    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 根据角色id更新角色菜单
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        //先通过角色id删除所有菜单id
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        //如 本就只有一个菜单id，将其删除(更新)，mids就为空
        if (null==mids || 0==mids.length){
            return true;
        }
        //自定义一个批量更新
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        if (result == mids.length){
            return true;
        }
        return false;
    }
}
