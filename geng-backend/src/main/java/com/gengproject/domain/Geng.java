package com.gengproject.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.List;

import com.gengproject.util.typeHandler.List2VarcharTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author liehuo
 * @since 2023-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tbl_geng",autoResultMap = true)
public class Geng implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ExcelProperty("简称")
    private String resume;

    @TableField(typeHandler = List2VarcharTypeHandler.class)
    private List<Integer> tagIds;

    private String src;

    private String srcType;

    @ExcelProperty("描述")
    private String description;

    private Integer createTime;

    private Integer updateTime;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;


}
