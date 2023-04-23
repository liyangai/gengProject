package com.gengproject.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gengproject.domain.Geng;
import com.gengproject.domain.Tag;
import com.gengproject.service.IGengService;
import com.gengproject.util.exception.BusinessException;
import com.gengproject.util.model.ExportGeng;
import com.gengproject.util.model.constant.HttpCode;
import com.gengproject.util.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    @PostMapping()
    public Result addByTagNames(@RequestBody String str){
        ObjectMapper mapper = new ObjectMapper();
        Geng geng = new Geng();
        List<String> childrenNames;
        try{
            Map<String, Object> map = mapper.readValue(str, Map.class);
            childrenNames = (List<String>) map.get("tagNames");
            Map<String, String> data =  (Map<String, String>)map.get("data");
            geng.setResume(data.get("resume"));
            geng.setDescription(data.get("description"));
            geng.setSrc(data.get("src"));
            geng.setSrcType(data.get("srcType"));
        }catch (Exception e){
            throw new BusinessException(HttpCode.ERROR,"参数错误");
        }
        geng.setId(null);

        boolean flag = gengService.opreateGengByTagNames(geng, childrenNames);
        return flag ? new Result(HttpCode.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }


    @PutMapping
    public Result put(@RequestBody String str){
        ObjectMapper mapper = new ObjectMapper();
        Geng geng = new Geng();
        List<String> childrenNames;
        try{
            Map<String, Object> map = mapper.readValue(str, Map.class);
            childrenNames = (List<String>) map.get("tagNames");
            Map<String, String> data =  (Map<String, String>)map.get("data");
            geng.setResume(data.get("resume"));
            geng.setDescription(data.get("description"));
            geng.setSrc(data.get("src"));
            geng.setId(Integer.parseInt(String.valueOf(data.get("id"))));
            geng.setSrcType(data.get("srcType"));
        }catch (Exception e){
            throw new BusinessException(HttpCode.ERROR,"参数错误");
        }

        boolean flag = gengService.opreateGengByTagNames(geng, childrenNames);
        return flag ? new Result(HttpCode.SUCCESS,geng): Result.getUnkonwnErrorResult();
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

        return new Result(HttpCode.SUCCESS,results);
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        boolean flag = false;
        try {
            flag = gengService.deleteById(id);
        }catch (Exception e){
            System.out.println(e);
        }

        return flag ? new Result(HttpCode.SUCCESS,null): Result.getUnkonwnErrorResult();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable String id){
        Geng geng = gengService.getById(id);
        return geng != null ? new Result(HttpCode.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

    @GetMapping("/test")
    public Result getByTagIdsss(@RequestBody List<Integer> tagIds, @RequestParam Boolean isAdd){
        if(isAdd == null){
            isAdd = true;
        }
        List<Geng> geng = gengService.getByTagIds(tagIds,isAdd);
        return geng != null ? new Result(HttpCode.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

    @PostMapping("/getByTagIds")
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
        return geng != null ? new Result(HttpCode.SUCCESS,geng): Result.getUnkonwnErrorResult();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        setExcelRespProp(response, "会员列表");
        List<Geng> memberList = gengService.list();
        List<ExportGeng> list = new ArrayList<>();
        for(Geng geng : memberList){
            ExportGeng exportGeng = new ExportGeng();
            exportGeng.setDesc(geng.getDescription());
            exportGeng.setResume(geng.getResume());
            list.add(exportGeng);
        }

        EasyExcel.write(response.getOutputStream())
                .head(ExportGeng.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("会员列表")
                .doWrite(list);
       }


    private void setExcelRespProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }
}

