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
@TableName("tbl_geng")
public class Geng implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String resume;

    private String tagIds;

    private String src;

    private String srcType;

    private String description;

    private String gengMsg1;

    private String gengMsg2;

    private String gengMsg3;

    private String gengMsg4;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;


}
