package com.gengproject.service;

import com.gengproject.dao.TagDao;
import com.gengproject.domain.Tag;
import com.gengproject.util.model.TagNode;
import com.gengproject.util.model.TagTree;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TagManagerService {

    public TagTree getTagTree();

    Tag getByTagName(String name);


    List<TagNode> removeTagChildren(List<Tag> tagNodes, TagTree tagTree);

    //去除list中存在的子节点
    List<TagNode> removeTagChildrenByIds(List<Integer> tgIds, TagTree tagTree);

    boolean hasParent(TagNode childNode, List<TagNode> tagNodes, HashMap<Integer, TagNode> tagIdMap, List<TagNode> allNodeList);
}
