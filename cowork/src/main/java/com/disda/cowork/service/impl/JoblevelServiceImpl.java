package com.disda.cowork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disda.cowork.mapper.JoblevelMapper;
import com.disda.cowork.po.Joblevel;
import com.disda.cowork.service.IJoblevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author disda
 * @since 2022-01-28
 */
@Service
public class JoblevelServiceImpl extends ServiceImpl<JoblevelMapper, Joblevel> implements IJoblevelService {

    @Autowired
    JoblevelMapper joblevelMapper;

    @Override
    public List<Joblevel> getLevels() {
        QueryWrapper<Joblevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT titleLevel");
        List<Joblevel> jls = joblevelMapper.selectList(queryWrapper);
        return jls;
    }
}
