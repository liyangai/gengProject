package com.gengproject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gengproject.domain.Geng;
import com.gengproject.service.IGengService;
import com.gengproject.util.exception.BusinessException;
import com.gengproject.util.model.Code;
import com.gengproject.util.model.Result;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        geng.setId(null);
        boolean flag = gengService.save(geng);
        return flag ? new Result(Code.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

    @PostMapping("/addByTagNmes")
    public Result addByTagNames(@RequestBody String str, HttpServletRequest request){
//        geng.setId(null);
//        boolean flag = gengService.save(geng);
        ObjectMapper mapper = new ObjectMapper();
        String tagName = request.getParameter("tagName");
        Geng geng;
        List<String> tagNames;
        try{

            Map<String, Object> map = mapper.readValue(str, Map.class);
            tagNames = (List<String>) map.get("tagNames");
            String re =  (String)map.get("data");
//            String re =  "{\"resume\":\"geng1\"}";

            geng = mapper.readValue(re,Geng.class);


        }catch (Exception e){
            throw new BusinessException(Code.ERROR,"参数错误");
        }

        boolean flag = gengService.addByTagNames(geng, tagNames);

        return flag ? new Result(Code.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

//    @PostMapping
//    public Result addByTagNames(@RequestBody Geng geng){
//        return  Result.getUnkonwnErrorResult();
//    }

    @PutMapping
    public Result put(@RequestBody Geng geng){
        boolean flag = gengService.modify(geng);
        return flag ? new Result(Code.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

    @DeleteMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Integer> ids){
        List<Map<Integer,Boolean>> results = new ArrayList<>();
        for (Integer id :ids){
            boolean flag = false;
            try{
                flag = gengService.deleteById(id);
            }catch (Exception e){

            }
            Map<Integer,Boolean> result = new HashMap<>();
            result.put(id,flag);
            results.add(result);
        }

        return new Result(Code.SUCCESS,results);
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        boolean flag = false;
        try {
            flag = gengService.deleteById(id);
        }catch (Exception e){
            System.out.println(e);
        }

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

