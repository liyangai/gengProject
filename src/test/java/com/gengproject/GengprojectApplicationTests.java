package com.gengproject;

import com.gengproject.dao.TagDao;
import com.gengproject.domain.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GengprojectApplicationTests {


    @Autowired
    TagDao tagDac;

    @Test
    void contextLoads() {
        Tag tag = new Tag() ;
        tag.setTagName("tag1");
        tagDac.insert(tag);
    }

}
