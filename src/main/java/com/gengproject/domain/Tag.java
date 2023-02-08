package com.gengproject.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author liehuo
 * @since 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String tagName;

    private String childIds;

    private String parentId;

    private String tagIcon;

    private String description;

    private String tagMsg1;

    private String tagMsg2;

    private String tagMsg3;

    private String tagMsg4;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;


}
