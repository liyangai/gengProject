package com.gengproject.controller;


import com.gengproject.domain.Geng;
import com.gengproject.service.IGengService;
import com.gengproject.util.model.Code;
import com.gengproject.util.model.Result;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
@RequestMapping("/geng")
public class GengController {

    @Autowired
    IGengService gengService;

    @PostMapping
    public Result add(@RequestBody Geng geng){
//        geng.setId(null);
        boolean flag = gengService.save(geng);
        return flag ? new Result(Code.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

//    @PostMapping
//    public Result addByTagNames(@RequestBody Geng geng){
//        return  Result.getUnkonwnErrorResult();
//    }

    @PutMapping
    public Result put(@RequestBody Geng geng){
        boolean flag = gengService.updateById(geng);
        return flag ? new Result(Code.SUCCESS,null): Result.getUnkonwnErrorResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        boolean flag = gengService.deleteById(id);
        return flag ? new Result(Code.SUCCESS,null): Result.getUnkonwnErrorResult();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable String id){
        Geng geng = gengService.getById(id);
        return geng != null ? new Result(Code.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

    @GetMapping("/test")
    public Result getByTagIdsss(@RequestBody List<Integer> tagIds, @RequestParam Boolean isAdd){
        if(isAdd == null){
            isAdd = true;
        }
        List<Geng> geng = gengService.getByTagIds(tagIds,isAdd);
        return geng != null ? new Result(Code.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

    @GetMapping()
    public Result getByTagIds(@RequestBody List<Integer> tagIds, @RequestParam(value = "isAdd",required=false) Boolean isAdd){
        if(isAdd == null){
            isAdd = true;
        }
        List<Geng> geng;
        if(tagIds == null || tagIds.size() == 0){
            geng = gengService.list();
        }else {
            geng = gengService.getByTagIds(tagIds,isAdd);
        }
        return geng != null ? new Result(Code.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }
}

