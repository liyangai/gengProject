package com.gengproject.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gengproject.domain.Geng;
import com.gengproject.domain.Tag;
import com.gengproject.service.ITagService;
import com.gengproject.util.exception.BusinessException;
import com.gengproject.util.model.constant.HttpCode;
import com.gengproject.util.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liehuo
 * @since 2023-02-09
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    ITagService tagService;

    @PostMapping
    public Result add(@RequestBody String str) {
        ObjectMapper mapper = new ObjectMapper();
        Tag tag = new Tag();
        String parentName;
        List<String> childrenNames;
        try{
            Map<String, Object> map = mapper.readValue(str, Map.class);
            childrenNames = (List<String>) map.get("childrenNames");
            parentName = (String)map.get("parentName");
            Map<String, String> data =  (Map<String, String>)map.get("data");
            tag.setTagName(data.get("tagName"));
            tag.setTagIcon(data.get("tagIcon"));
            tag.setDescription(data.get("description"));
        }catch (Exception e){
            throw new BusinessException(HttpCode.ERROR,"参数错误");
        }
        tag.setId(null);

        boolean flag = tagService.operateTagByTagName(tag, parentName, childrenNames);

        return flag ? new Result(HttpCode.SUCCESS,tag): Result.getUnkonwnErrorResult();

    }

    @PutMapping
    public Result put(@RequestBody String str){
        ObjectMapper mapper = new ObjectMapper();
        Tag tag = new Tag();
        String parentName;
        List<String> childrenNames;
        try{
            Map<String, Object> map = mapper.readValue(str, Map.class);
            childrenNames = (List<String>) map.get("childrenNames");
            parentName = (String)map.get("parentName");
            Map<String, Object> data =  (Map<String, Object>)map.get("data");
            tag.setTagName((String) data.get("tagName"));
            tag.setTagIcon((String)data.get("tagIcon"));
            tag.setId(Integer.parseInt(String.valueOf(data.get("id"))));
            tag.setDescription((String)data.get("description"));

        }catch (Exception e){
            throw new BusinessException(HttpCode.ERROR,"参数错误");
        }

        boolean flag = tagService.operateTagByTagName(tag, parentName, childrenNames);

        return flag ? new Result(HttpCode.SUCCESS,tag): Result.getUnkonwnErrorResult();
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        boolean flag = tagService.deleteTagById(id);
        return flag ? new Result(HttpCode.SUCCESS,null): Result.getUnkonwnErrorResult();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable String id){
        Tag tag = tagService.getById(id);
        return tag != null ? new Result(HttpCode.SUCCESS,tag): Result.getUnkonwnErrorResult();
    }


    @GetMapping()
    public Result getList(@RequestParam(value = "tagName",required=false) String tagName){
        List<Tag> tag;
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(tagName), "tagName", tagName);
        tag = tagService.list(queryWrapper);
        return tag != null ? new Result(HttpCode.SUCCESS,tag): Result.getUnkonwnErrorResult();
    }
}

