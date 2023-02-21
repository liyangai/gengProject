package com.gengproject.util.model;

import com.gengproject.domain.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagNode extends Tag {

    public  TagNode(){

    }

    public TagNode(Tag tag){
        this.setId(tag.getId());
        this.setTagName(tag.getTagName());
        this.setParentId(tag.getParentId());
        this.setCreateTime(tag.getCreateTime());
        this.setUpdateTime(tag.getUpdateTime());
        this.setTagIcon(tag.getTagIcon());
        this.setDescription(tag.getDescription());
    }

    private List<TagNode> childrenNode;

    private  TagNode parentNode;

    public List<TagNode> getChildrenNode() {
        return childrenNode;
    }

    public void setChildrenNode(List<TagNode> childrenNode) {
        this.childrenNode = childrenNode;
    }

    public void addChildrenNode(TagNode node) {
        if(node == null){
            return;
        }
        if(this.childrenNode == null) {
            this.childrenNode = new ArrayList<TagNode>();
        }
        this.childrenNode.add(node);
    }

    public TagNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(TagNode parentNode) {
        this.parentNode = parentNode;
    }

    public void removeChild(TagNode tagNode){
        if(this.childrenNode == null){
            return;
        }
        this.childrenNode.remove(tagNode);
    }
}
