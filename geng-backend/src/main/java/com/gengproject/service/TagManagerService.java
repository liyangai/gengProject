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
}
