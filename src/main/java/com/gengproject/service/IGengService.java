package com.gengproject.service;

import com.gengproject.domain.Geng;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor=Exception.class)
    boolean addByTagNames(Geng geng, List<String> tagNames);

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
