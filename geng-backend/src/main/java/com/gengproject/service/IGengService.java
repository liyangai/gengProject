package com.gengproject.service;

import com.gengproject.domain.Geng;
import com.baomidou.mybatisplus.extension.service.IService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    boolean opreateGengByTagNames(Geng geng, List<String> tagNames);

    boolean deleteById(Integer id);

    /**
     *
     * @param tagIds
     * @param isAnd 是否为交集
     * @return
     */
    List<Geng> getByTagIds(List<Integer> tagIds,boolean isAnd);

    void autoAddGeng() throws TesseractException;

    void autoAddGengByBaidu() throws Exception;
}
