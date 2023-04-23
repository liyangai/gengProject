package com.gengproject.util.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ExportGeng {

    @ExcelProperty("resume")
    @ColumnWidth(20)
    private String resume;


    @ExcelProperty("desc")
    @ColumnWidth(20)
    private String desc;

}
