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

    private HashMap<Integer, TagNode> tagIdMap = new HashMap<>();
    private List<TagNode> nodeList = new ArrayList<TagNode>();

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

    @Override
    public boolean operateTagByTagName(Tag tag, String parentTagName, List<String> childrenNames){
        if(tag.getId() ==null){
            addTag(tag);
        }else {
            checkTagName(tag);
        }
        tag.setParentId(null);
        Tag parent = getOrAddTagByName(parentTagName);
        List<Tag> children = new ArrayList<>();
        for (String childrenName : childrenNames) {
            children.add(getOrAddTagByName(childrenName));
        }




        TagTree tagTree = tagManagerService.getTagTree();
        HashMap<Integer, TagNode> tagIdMap = tagTree.getTagIdMap();
        List<TagNode> nodeList = tagTree.getNodeList();


        tag.setParentId(parent.getParentId());

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

    public void checkTagName(Tag tag){
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
