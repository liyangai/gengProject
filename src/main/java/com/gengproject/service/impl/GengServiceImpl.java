package com.gengproject.service.impl;

import com.gengproject.dao.TagDao;
import com.gengproject.domain.Geng;
import com.gengproject.dao.GengDao;
import com.gengproject.domain.Tag;
import com.gengproject.service.IGengService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gengproject.util.model.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2023-02-09
 */
@Service
public class GengServiceImpl extends ServiceImpl<GengDao, Geng> implements IGengService {

    @Autowired
    private GengDao gengDao;

    @Autowired
    private TagDao tagDao;

    @Override
    public void addByTagIds(Geng geng) {
        int insert = gengDao.insert(geng);
        System.out.println(insert);
        System.out.println(geng);
    }


    private List<TagNode> getTagTree() {
        List<Tag> tagList = tagDao.selectList(null);
        List<TagNode> nodeList = new ArrayList<TagNode>();
        for(Tag tag : tagList){
            TagNode tagNode = new TagNode(tag);
            nodeList.add(tagNode);
        }
        return nodeList;
    }
}
