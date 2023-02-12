package com.gengproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gengproject.domain.Tag;
import com.gengproject.dao.TagDao;
import com.gengproject.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gengproject.service.TagManagerService;
import com.gengproject.util.exception.BusinessException;
import com.gengproject.util.model.Code;
import com.gengproject.util.model.TagNode;
import com.gengproject.util.model.TagTree;
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

    private HashMap<Integer, TagNode> tagIdMap = new HashMap<>();
    private List<TagNode> nodeList = new ArrayList<TagNode>();

    @Override
    public boolean addTag(Tag tag){
        if(tag.getTagName() == null || tag.getTagName() == ""){
            throw new BusinessException(Code.ERROR,"tag名不能为空");
        }
        Tag selectOne = tagManagerService.getByTagName(tag.getTagName());
        if(selectOne != null){
            throw new BusinessException(Code.ERROR,"tag名不能重复");
        }
        int flag = tagDao.insert(tag);
        return  flag == 1;
    }

    @Override
//    @Transactional(rollbackFor=Exception.class)
    public boolean updateTag(Tag tag) {
        Tag oldTag = tagDao.selectById(tag.getId());
        if(oldTag == null){
            throw new BusinessException(Code.ERROR,"tag不存在");
        }
        Tag sameNameTag = tagManagerService.getByTagName(tag.getTagName());
        if(sameNameTag != null && sameNameTag.getId() != tag.getId()){
            throw new BusinessException(Code.ERROR,"tag名已存在");
        }
        Integer parentId = tag.getParentId();
        if(parentId != null){
            Tag parentTag  = tagDao.selectById(parentId);
            if(parentTag == null){
                throw new BusinessException(Code.ERROR,"父tag不存在");
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
            throw new BusinessException(Code.ERROR,"tag不存在");
        }
        List<Tag> childrenTag = getChildrenTag(id);
        if(childrenTag != null && childrenTag.size() > 0){
            throw new BusinessException(Code.ERROR,"存在子tag，不允许删除");
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
