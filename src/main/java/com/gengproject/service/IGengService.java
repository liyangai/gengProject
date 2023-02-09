package com.gengproject.service;

import com.gengproject.domain.Geng;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liehuo
 * @since 2023-02-09
 */
public interface IGengService extends IService<Geng> {
    public void addByTagIds(Geng geng);
}
