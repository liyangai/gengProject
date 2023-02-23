package com.gengproject.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gengproject.dao.TagDao;
import com.gengproject.domain.Geng;
import com.gengproject.dao.GengDao;
import com.gengproject.domain.Tag;
import com.gengproject.service.IGengService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gengproject.service.ITagService;
import com.gengproject.service.TagManagerService;
import com.gengproject.util.exception.BusinessException;
import com.gengproject.util.http.Base64Util;
import com.gengproject.util.http.FileUtil;
import com.gengproject.util.http.HttpUtil;
import com.gengproject.util.model.constant.GengType;
import com.gengproject.util.model.constant.HttpCode;
import com.gengproject.util.model.TagNode;
import com.gengproject.util.model.TagTree;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
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

    @Autowired
    TagManagerService tagManagerService;

    @Autowired
    ITagService tagService;


    @Override
    public boolean addByTagIds(Geng geng) {

        int insert = gengDao.insert(geng);
        return insert == 1;

    }

    @Transactional(rollbackFor=Exception.class)
    public boolean opreateGengByTagNames(Geng geng, List<String> tagNames){
        List<Tag> children = new ArrayList<>();
        for (String childrenName : tagNames) {
            children.add(tagManagerService.getOrAddTagByName(childrenName));
        }
        TagTree tagTree = tagManagerService.getTagTree();
        List<TagNode> childrenNodes = tagManagerService.removeTagChildren(children,tagTree);
        List<Integer> idList = new ArrayList<>();
        for (TagNode childrenNode : childrenNodes) {
            idList.add(childrenNode.getId());
        }
        geng.setTagIds(idList);
        int insert = 0;
        if(geng.getId() == null){
            insert = gengDao.insert(geng);
        }else {
            checkGengExit(geng);
            insert = gengDao.updateById(geng);
        }
        return insert == 1;
    }

    @Override
    public boolean deleteById(Integer id){
        Geng geng = gengDao.selectById(id);
        if(geng == null){
            throw  new BusinessException(HttpCode.ERROR,"geng 不存在");
        }
        int flag = gengDao.deleteById(id);
        return flag == 1 ;
    }

    /**
     *
     * @param tagIds
     * @param isAnd 是否为交集
     * @return
     */
    @Override
    public List<Geng> getByTagIds(List<Integer> tagIds, boolean isAnd) {
        TagTree tagTree = tagManagerService.getTagTree();
        List<TagNode> childrenNodes = tagManagerService.removeTagChildrenByIds(tagIds,tagTree);
        HashMap<Integer, TagNode> tagIdMap = tagTree.getTagIdMap();
        List<Geng> allGenglist = this.list();
        Map<Integer,Geng> gengIdMap = new HashMap<>();
        for(Geng geng : allGenglist){
            gengIdMap.put(geng.getId(),geng);
        }
        List<Set<Integer>> GengIdsList = new ArrayList<>();
        for(TagNode childNode : childrenNodes){
            GengIdsList.add(this.getGengIdsByOneTagId(childNode.getId(),tagIdMap,allGenglist));
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
                    set.add(gengId);
                }
            }
            resultIds.addAll(set);
        }

        List<Geng> result = new ArrayList<>();
        for(Integer id : resultIds){
            result.add(gengIdMap.get(id));
        }

        return result;
    }




    private Set<Integer> getGengIdsByOneTagId(Integer tagId,HashMap<Integer, TagNode> tagIdMap,List<Geng> allGenglist){
        TagNode parent = tagIdMap.get(tagId);
        List<TagNode> allChildrenTags = this.getAllChildren(parent);
        allChildrenTags.add(parent);
        Set<Integer> result = new HashSet<>();
        for(Geng geng : allGenglist){
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

    @Override
    public void autoAddGeng() throws TesseractException {
        String baseDir = System.getProperty("user.dir");
        String dataPath = baseDir + "\\src\\main\\resources\\javaResource\\tessdata-master";
        String imageDirPath = baseDir + "\\src\\main\\resources\\static\\temimage";
        ITesseract instance = new Tesseract();
        // 语言库位置
        instance.setDatapath(dataPath);
        // 中英文库
        instance.setLanguage("eng+chi_sim");
        // 简体中文库
//        instance.setLanguage("chi_sim");
        // 待识别的图片路径
        File imageLocation = new File(imageDirPath);
        for (File image : imageLocation.listFiles()) {
            String description = instance.doOCR(image);
            Geng geng = new Geng();
            geng.setDescription(description);
            geng.setSrc(image.getName());
            geng.setSrcType(GengType.AUTO_ADD);
            Tag tag = tagManagerService.getOrAddTagByName(GengType.AUTO_ADD);
            geng.setTagIds(new ArrayList<Integer>(){{add(tag.getId());}});
            gengDao.insert(geng);

            System.out.println(image.getName() + "---" + instance.doOCR(image));
            System.out.println("-----------------------------------------------------");
        };
    }

    @Override
    public void autoAddGengByBaidu() throws Exception {
        String baseDir = System.getProperty("user.dir");
        String dataPath = baseDir + "\\src\\main\\resources\\javaResource\\tessdata-master";
        String imageDirPath = baseDir + "\\src\\main\\resources\\static\\temimage";
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        // 待识别的图片路径
        File imageLocation = new File(imageDirPath);
        for (File image : imageLocation.listFiles()) {
            byte[] imgData = FileUtil.readFileByBytes(image.getPath());
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = "24.1df65347b34a0142ef47d1f59f88a568.2592000.1678884509.282335-30398225";

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(result, Map.class);
            List<Map<String,String>> list =  (List<Map<String,String>>)map.get("words_result");
            StringBuilder resultString = new StringBuilder();
            for(Map<String,String> item :list){
                resultString.append(item.get("words"));
                resultString.append("\n");
            }
            Geng geng = new Geng();
            geng.setDescription(resultString.toString());
            geng.setSrc(image.getName());
            geng.setSrcType(GengType.AUTO_ADD);
            Tag tag = tagManagerService.getOrAddTagByName(GengType.AUTO_ADD);
            geng.setTagIds(new ArrayList<Integer>(){{add(tag.getId());}});
            gengDao.insert(geng);
            System.out.println(resultString);
            Thread.sleep(3*1000);
        };
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


    private void checkGengExit(Geng geng){
        Geng geng1 = gengDao.selectById(geng.getId());
        if(geng1 == null){
            throw new BusinessException(HttpCode.ERROR,"geng 不存在");
        }else {
            geng.setVersion(geng1.getVersion());
        }
    }

}
