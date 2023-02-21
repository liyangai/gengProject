package com.gengproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gengproject.domain.Tag;
import com.gengproject.dao.TagDao;
import com.gengproject.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gengproject.service.TagManagerService;
import com.gengproject.util.exception.BusinessException;
import com.gengproject.util.model.TagTree;
import com.gengproject.util.model.constant.HttpCode;
import com.gengproject.util.model.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements ITagService {

    @Autowired
     TagDao tagDao;

    @Autowired
    TagManagerService tagManagerService;


    @Override
    public boolean addTag(Tag tag){
        if(tag.getTagName() == null || tag.getTagName() == ""){
            throw new BusinessException(HttpCode.ERROR,"tag名不能为空");
        }
        Tag selectOne = tagManagerService.getByTagName(tag.getTagName());
        if(selectOne != null){
            throw new BusinessException(HttpCode.ERROR,"tag名不能重复");
        }
        int flag = tagDao.insert(tag);
        return  flag == 1;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean operateTagByTagName(Tag tag, String parentTagName, List<String> childrenNames){
        Integer oldParentId = null;
        if(tag.getId() ==null){
            addTag(tag);
        }else {
            oldParentId = checkTagName(tag).getParentId();
        }

        tag.setParentId(null);

        Tag parent = getOrAddTagByName(parentTagName);
        List<Tag> children = new ArrayList<>();
        for (String childrenName : childrenNames) {
            children.add(getOrAddTagByName(childrenName));
        }
        TagTree tagTree = tagManagerService.getTagTree();


        HashMap<Integer, TagNode> tagIdMap = tagTree.getTagIdMap();
        List<TagNode> allNodeList = tagTree.getNodeList();

        TagNode tagNode = tagIdMap.get(tag.getId());

        TagNode oldParentNode = tagIdMap.get(oldParentId);
        if(oldParentNode != null){
            oldParentNode.removeChild(tagNode);
        }

        List<TagNode> childrenNodes = tagManagerService.removeTagChildren(children,tagTree);

        //检查父tag是否合法
        TagNode parentNode = tagIdMap.get(parent.getId());

        if(parentNode.getId() == tagNode.getId()){
            throw new BusinessException(HttpCode.ERROR,"父tag不能与此tag相同");
        }

        if(tagManagerService.hasParent(parentNode,new ArrayList<TagNode>(){{add(tagNode);}},tagIdMap,allNodeList)){
            throw new BusinessException(HttpCode.ERROR,"父tag不能为此tag的子级");
        }


        tag.setParentId(parent.getId());

        int i = tagDao.updateById(tag);
        if(i != 1){
            throw new BusinessException(HttpCode.ERROR,"父tag修改失败");
        }
        tagNode.setParentId(parent.getId());
        tagNode.setParentNode(parentNode);
        parentNode.addChildrenNode(tagNode);

        for (TagNode childNode : childrenNodes) {
            if(childNode.getId() == tagNode.getId()){
                throw new BusinessException(HttpCode.ERROR,"子tag不能与此tag相同");
            }
            if(tagManagerService.hasParent(tagNode,new ArrayList<TagNode>(){{add(childNode);}},tagIdMap,allNodeList)){
                throw new BusinessException(HttpCode.ERROR,"子tag不能为此tag的父级,子tag名："+childNode.getTagName());
            }
        }


        for (TagNode childrenNode : childrenNodes) {
            childrenNode.setParentId(tag.getId());
            int j = tagDao.updateById(childrenNode);
            if(j != 1){
                throw new BusinessException(HttpCode.ERROR,"子tag不能为此tag的父级,子tag名："+childrenNode.getTagName());
            }
        }

        return true;

    }


    public Tag getOrAddTagByName(String name){
        if(name == null || name == ""){
            throw new BusinessException(HttpCode.ERROR,"tag名不能为空");
        }
        Tag selectOne = tagManagerService.getByTagName(name);

        if(selectOne == null){
            selectOne = new Tag();
            selectOne.setTagName(name);
            tagDao.insert(selectOne);
        }
        return selectOne;
    }

    public Tag checkTagName(Tag tag){
        if(tag.getTagName() == null || tag.getTagName() == ""){
            throw new BusinessException(HttpCode.ERROR,"tag名不能为空");
        }
        Tag oldTag = tagDao.selectById(tag.getId());
        if(oldTag == null){
            throw new BusinessException(HttpCode.ERROR,"tag不存在");
        }
        Tag sameNameTag = tagManagerService.getByTagName(tag.getTagName());
        if(sameNameTag != null && sameNameTag.getId() != tag.getId()){
            throw new BusinessException(HttpCode.ERROR,"tag名已存在");
        }
        return oldTag;
    }

    @Override
//    @Transactional(rollbackFor=Exception.class)
    public boolean updateTag(Tag tag) {
        Tag oldTag = tagDao.selectById(tag.getId());
        if(oldTag == null){
            throw new BusinessException(HttpCode.ERROR,"tag不存在");
        }
        Tag sameNameTag = tagManagerService.getByTagName(tag.getTagName());
        if(sameNameTag != null && sameNameTag.getId() != tag.getId()){
            throw new BusinessException(HttpCode.ERROR,"tag名已存在");
        }
        Integer parentId = tag.getParentId();
        if(parentId != null){
            Tag parentTag  = tagDao.selectById(parentId);
            if(parentTag == null){
                throw new BusinessException(HttpCode.ERROR,"父tag不存在");
            }
        }
        tag.setVersion(oldTag.getVersion());
        int flag = tagDao.updateById(tag);
        return  flag == 1;
    }

    @Override
    public boolean deleteTagById(Integer id){
        Tag tag = tagDao.selectById(id);
        if(tag == null){
            throw new BusinessException(HttpCode.ERROR,"tag不存在");
        }
        List<Tag> childrenTag = getChildrenTag(id);
        if(childrenTag != null && childrenTag.size() > 0){
            throw new BusinessException(HttpCode.ERROR,"存在子tag，不允许删除");
        }
        int flag = tagDao.deleteById(tag);
        return  flag == 1;
    }


    private List<Tag> getChildrenTag(Integer id){
        LambdaQueryWrapper<Tag> lqm = new LambdaQueryWrapper<>();
        lqm.eq(Tag::getParentId, id);
        List<Tag> tagList = tagDao.selectList(lqm);
        return tagList;
    }
}
