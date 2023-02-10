package com.gengproject.service.impl;

import com.gengproject.dao.TagDao;
import com.gengproject.domain.Geng;
import com.gengproject.dao.GengDao;
import com.gengproject.domain.Tag;
import com.gengproject.service.IGengService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gengproject.util.model.TagNode;
import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xerces.internal.dom.ChildNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private HashMap<Integer, TagNode> tagIdMap = new HashMap<>();
    private List<TagNode> nodeList = new ArrayList<TagNode>();

    List<Geng> allGenglist = new ArrayList<>();

    private HashMap<Integer, Geng> gengIdMap = new HashMap<>();

    @Override
    public boolean addByTagIds(Geng geng) {
        int insert = gengDao.insert(geng);
        return insert == 1;

    }

    /**
     *
     * @param tagIds
     * @param isAnd 是否为交集
     * @return
     */
    @Override
    public List<Geng> getByTagIds(List<Integer> tagIds, boolean isAnd) {

        List<Integer> newIds = this.removeChildrenTagIds(tagIds);
        allGenglist = this.list();
        this.gengIdMap = new HashMap<>();
        for(Geng geng : allGenglist){
            this.gengIdMap.put(geng.getId(),geng);
        }
        List<Set<Integer>> GengIdsList = new ArrayList<>();
        for(Integer newId : newIds){
            GengIdsList.add(this.getGengIdsByOneTagId(newId));
        }
        List<Integer> resultIds = new ArrayList<>();
        if(isAnd){
            int size = GengIdsList.size();
            Map<Integer,Integer> idExitTimesMap = new HashMap<>();
            for(Set<Integer> gengIds : GengIdsList){
                for(Integer gengId : gengIds){
                    Integer gengTimes = idExitTimesMap.get(gengId);
                    if(gengTimes == null){
                        idExitTimesMap.put(gengId,1);
                    }else {
                        idExitTimesMap.put(gengId, gengTimes + 1);
                    }
                }
            }

            for(Map.Entry<Integer,Integer> entry : idExitTimesMap.entrySet()){
                if(entry.getValue() == size){
                    resultIds.add(entry.getKey());
                }
            }

        }else {
            Set<Integer> set = new HashSet<>();
            for(Set<Integer> gengIds : GengIdsList){
                for(Integer gengId : gengIds){
                    resultIds.add(gengId);
                }
            }
            resultIds.addAll(set);
        }

        List<Geng> result = new ArrayList<>();
        for(Integer id : resultIds){
            result.add(this.allGenglist.get(id));
        }

        return result;
    }


    private Set<Integer> getGengIdsByOneTagId(Integer tagId){
        TagNode parent = this.tagIdMap.get(tagId);
        List<TagNode> allChildrenTags = this.getAllChildren(parent);
        allChildrenTags.add(parent);
        Set<Integer> result = new HashSet<>();
        for(Geng geng : this.allGenglist){
            List<Integer> tagIds = geng.getTagIds();
            if(tagIds == null){
                continue;
            }
            for(Integer gengTagId: tagIds){
                TagNode tagNode = allChildrenTags.stream().filter(item -> item.getId() == gengTagId).findFirst().orElse(null);
                if(tagNode != null){
                    result.add(geng.getId());
                }
            }
        }
        return result;
    }


    private List<Integer> removeChildrenTagIds(List<Integer> tgIds){
        List<Integer> newList = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        ids.addAll(tgIds);
        if(ids == null || ids.size() == 0){
            return newList;
        }
        this.getTagTree();
        for(Integer id : ids){
            if(!hasParent(id,ids)){
                newList.add(id);
            }
        }
        return newList;
    }


    private boolean hasParent(Integer childId,Set<Integer> ids){
        TagNode childNode = this.tagIdMap.get(childId);
        if(childNode == null){
            return false;
        }
        List<TagNode> allParent = this.getAllParent(childNode);
        return this.checkNodeInTagIds(allParent,ids);
    }

    //获取所有父辈
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

    private boolean checkNodeInTagIds(List<TagNode> nodes, Set<Integer> ids){
        for(Integer id : ids){
            TagNode ch = nodes.stream().filter(tagNode -> tagNode.getId() == id).findFirst().orElse(null);
            if(ch != null){
                return true;
            }
        }
        return  false;
    }


    private List<Integer> removeAncestorTagIds(List<Integer> tgIds){
        Set<Integer> ids = new HashSet<>();
        ids.addAll(tgIds);
        List<Integer> newList = new ArrayList<>();
        if(ids == null || ids.size() == 0){
            return newList;
        }
        this.getTagTree();
        for(Integer id : ids){
            if(!hasParent(id,ids)){
                newList.add(id);
            }
        }
        return newList;
    }




    private boolean hasChild(Integer parentId,Set<Integer> ids){
        TagNode parentNode = this.tagIdMap.get(parentId);
        if(parentNode == null){
            return false;
        }
        List<TagNode> allChildren = this.getAllChildren(parentNode);
        return this.checkNodeInTagIds(allChildren,ids);
    }

    //获取所有子辈
    private List<TagNode> getAllChildren(TagNode node){
        List<TagNode> result = new ArrayList<>();
        List<TagNode> childrenNode = node.getChildrenNode();
        if(childrenNode == null){
            return result;
        }
        result.addAll(childrenNode);
        for(TagNode childNode: childrenNode){
            result.addAll(getAllChildren(childNode));
        }
        return  result;
    }

    private void getTagTree() {
        tagIdMap = new HashMap<>();
        nodeList = new ArrayList<TagNode>();
        List<Tag> tagList = tagDao.selectList(null);
        if(tagList == null){
            return ;
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
            List<Integer> childIds = node.getChildIds();
            if(childIds == null){
                continue;
            }
            for(Integer childId : childIds){
                TagNode childNode = tagIdMap.get(childId);
                node.addChildrenNode(childNode);
            }
        }
    }
}
