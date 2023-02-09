package com.gengproject.controller;


import com.alibaba.druid.util.ListDG;
import com.gengproject.domain.Geng;
import com.gengproject.service.IGengService;
import com.gengproject.util.exception.BusinessException;
import com.gengproject.util.model.Code;
import com.gengproject.util.model.Result;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
        geng.setId(16);
        gengService.addByTagIds(geng);
        return  null;
//        return "success";
    }
}

