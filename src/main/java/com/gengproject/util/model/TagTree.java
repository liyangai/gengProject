package com.gengproject.util.model;

import java.util.HashMap;
import java.util.List;

public class TagTree {
    private HashMap<Integer, TagNode> tagIdMap;
    private List<TagNode> nodeList;

    public HashMap<Integer, TagNode> getTagIdMap() {
        return tagIdMap;
    }

    public void setTagIdMap(HashMap<Integer, TagNode> tagIdMap) {
        this.tagIdMap = tagIdMap;
    }

    public List<TagNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<TagNode> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public String toString() {
        return "TagTree{" +
                "tagIdMap=" + tagIdMap +
                ", nodeList=" + nodeList +
                '}';
    }
}
