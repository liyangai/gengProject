package com.gengproject.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.util.List;

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
@TableName("tbl_tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String tagName;

    private List<Integer> childIds;

    private Integer parentId;

    private String tagIcon;

    private String description;

    private Integer createTime;

    private Integer updateTime;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;


}
