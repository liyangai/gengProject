package com.gengproject.service;

import com.gengproject.domain.Tag;
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
public interface ITagService extends IService<Tag> {
    boolean addTag(Tag tag);

    boolean operateTagByTagName(Tag tag, String parentTagName, List<String> childrenNames);

    public boolean updateTag(Tag tag);

    boolean deleteTagById(Integer id);
}
