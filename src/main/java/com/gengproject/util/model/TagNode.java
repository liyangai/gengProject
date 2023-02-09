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
        this.setChildIds(tag.getChildIds());
        this.setParentId(this.getParentId());
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
}
