package com.gengproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gengproject.dao.TagDao;
import com.gengproject.domain.Tag;
import com.gengproject.service.TagManagerService;
import com.gengproject.util.exception.BusinessException;
import com.gengproject.util.model.TagNode;
import com.gengproject.util.model.TagTree;
import com.gengproject.util.model.constant.HttpCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
                parentNode.addChildrenNode(node);
            }

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

    @Override
    public List<TagNode> removeTagChildren(List<Tag> tagNodes, TagTree tagTree){
        List<Integer> tagIds = new ArrayList<>();
        for (Tag tagNode : tagNodes) {
            tagIds.add(tagNode.getId());
        }
        return removeTagChildrenByIds(tagIds,tagTree);

    }

    @Override
    //??????list?????????????????????
    public List<TagNode> removeTagChildrenByIds(List<Integer> tgIds, TagTree tagTree){
        List<TagNode> newList = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        ids.addAll(tgIds);
        if(ids == null || ids.size() == 0){
            return newList;
        }
        HashMap<Integer, TagNode> tagIdMap = tagTree.getTagIdMap();
        List<TagNode> allNodeList = tagTree.getNodeList();
        List<TagNode> tagNodes = new ArrayList<>();
        for (Integer id : ids) {
            TagNode tagNode = tagIdMap.get(id);
            if(tagNode != null){
                tagNodes.add(tagNode);
            }
        }
        if(tagNodes == null || tagNodes.size() == 0){
            return newList;
        }

        for(TagNode node : tagNodes){
            if(!hasParent(node,tagNodes,tagIdMap,allNodeList)){
                newList.add(node);
            }
        }
        return newList;
    }

    /**
     * ??????tagNodes????????????childNode????????????
     * @param childNode
     * @param tagNodes
     * @param tagIdMap
     * @param allNodeList
     * @return
     */
    @Override
    public boolean hasParent(TagNode childNode, List<TagNode> tagNodes, HashMap<Integer, TagNode> tagIdMap, List<TagNode> allNodeList) {
        List<TagNode> allParent = this.getAllParent(childNode);
        return this.checkNodeInTags(allParent,tagNodes);

    }


    //??????????????????
    private List<TagNode> getAllParent(TagNode node){

        List<TagNode> result = new ArrayList<>();
        TagNode parentNode = node.getParentNode();
        if(parentNode == null){
            return result;
        }
        result.add(parentNode);
        result.addAll(getAllParent(parentNode));

        return  result;
    }

    private boolean checkNodeInTags(List<TagNode> allParent, List<TagNode> selectedtagNodes){
        for(TagNode selectedNode : selectedtagNodes){
            TagNode ch = allParent.stream().filter(tagNode -> tagNode.getId() == selectedNode.getId()).findFirst().orElse(null);
            if(ch != null){
                return true;
            }
        }
        return  false;
    }

    @Override
    public Tag getOrAddTagByName(String name){
        if(name == null || name == ""){
            throw new BusinessException(HttpCode.ERROR,"tag???????????????");
        }
        Tag selectOne = getByTagName(name);

        if(selectOne == null){
            selectOne = new Tag();
            selectOne.setTagName(name);
            tagDao.insert(selectOne);
        }
        return selectOne;
    }
}
