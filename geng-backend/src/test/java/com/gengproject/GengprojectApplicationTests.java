package com.gengproject;

import com.gengproject.dao.TagDao;
import com.gengproject.domain.Tag;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

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

    @Test
    void testTess4J() throws TesseractException {
        ITesseract instance = new Tesseract();
        // 语言库位置
        instance.setDatapath("D:\\test\\IdeaProjects\\gengProject\\src\\main\\resources\\javaResource\\tessdata-master");
        // 中英文库
//        instance.setLanguage("eng+chi_sim");
        // 简体中文库
        instance.setLanguage("chi_sim");
        // 待识别的图片路径
        File imageLocation = new File("D:\\test\\IdeaProjects\\gengProject\\src\\main\\resources\\javaResource\\gengImages");
        for (File image : imageLocation.listFiles()) {
            System.out.println(image.getName() + "---" + instance.doOCR(image));
        }
    }

}
