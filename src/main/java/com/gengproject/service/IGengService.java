package com.gengproject.service;

import com.gengproject.domain.Geng;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liehuo
 * @since 2023-02-09
 */
public interface IGengService extends IService<Geng> {
    public boolean addByTagIds(Geng geng);

    boolean deleteById(Integer id);

    boolean modify(Geng geng);

    /**
     *
     * @param tagIds
     * @param isAnd 是否为交集
     * @return
     */
    List<Geng> getByTagIds(List<Integer> tagIds,boolean isAnd);
}
