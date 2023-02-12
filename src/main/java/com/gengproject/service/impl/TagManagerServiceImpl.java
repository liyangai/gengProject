package com.gengproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gengproject.dao.TagDao;
import com.gengproject.domain.Tag;
import com.gengproject.service.TagManagerService;
import com.gengproject.util.model.TagNode;
import com.gengproject.util.model.TagTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TagManagerServiceImpl implements TagManagerService {

    @Autowired
    private TagDao tagDao;

    public TagTree getTagTree() {
        HashMap<Integer, TagNode> tagIdMap = new HashMap<>();
        List<TagNode> nodeList = new ArrayList<TagNode>();
        List<Tag> tagList = tagDao.selectList(null);
        if(tagList == null){
            return null;
        }

        for(Tag tag : tagList){
            TagNode tagNode = new TagNode(tag);
            nodeList.add(tagNode);
            tagIdMap.put(tagNode.getId(),tagNode);
        }
        for(TagNode node : nodeList){
            TagNode parentNode = tagIdMap.get(node.getParentId());
            if(parentNode != null){
                node.setParentNode(parentNode);
            }
            parentNode.addChildrenNode(node);
        }

        TagTree tagTree = new TagTree();
        tagTree.setTagIdMap(tagIdMap);
        tagTree.setNodeList(nodeList);
        return  tagTree;
    }

    @Override
    public Tag getByTagName(String name){
        LambdaQueryWrapper<Tag> lqm = new LambdaQueryWrapper<>();
        lqm.eq(Tag::getTagName,name);
        Tag selectOne = tagDao.selectOne(lqm);
        return selectOne;
    }
}
