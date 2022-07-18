package com.disda.cowork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disda.cowork.po.Joblevel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author disda
 * @since 2022-01-28
 */
public interface IJoblevelService extends IService<Joblevel> {

    List<Joblevel> getLevels();
}
