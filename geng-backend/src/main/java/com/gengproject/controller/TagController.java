package com.gengproject.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gengproject.domain.Tag;
import com.gengproject.service.ITagService;
import com.gengproject.util.model.Code;
import com.gengproject.util.model.Result;
import com.gengproject.domain.Tag;
import com.gengproject.domain.Tag;
import com.gengproject.service.ITagService;
import com.gengproject.service.ITagService;
import com.gengproject.util.model.Code;
import com.gengproject.util.model.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result add(@RequestBody Tag tag){
        tag.setId(null);
        boolean flag = tagService.addTag(tag);
        return flag ? new Result(Code.SUCCESS,tag): Result.getUnkonwnErrorResult();
    }

    @PutMapping
    public Result put(@RequestBody Tag tag){
        boolean flag = tagService.updateTag(tag);
        return flag ? new Result(Code.SUCCESS,tag): Result.getUnkonwnErrorResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        boolean flag = tagService.deleteTagById(id);
        return flag ? new Result(Code.SUCCESS,null): Result.getUnkonwnErrorResult();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable String id){
        Tag tag = tagService.getById(id);
        return tag != null ? new Result(Code.SUCCESS,tag): Result.getUnkonwnErrorResult();
    }


    @GetMapping()
    public Result getList(@RequestParam(value = "tagName",required=false) String tagName){
        List<Tag> tag;
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(tagName), "tagName", tagName);
        tag = tagService.list(queryWrapper);
        return tag != null ? new Result(Code.SUCCESS,tag): Result.getUnkonwnErrorResult();
    }
}

