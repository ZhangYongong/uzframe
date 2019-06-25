package com.ughen.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 角色表
 *
 * @Author:Yonghong Zhang
 * @Date: 10:43 2019/1/28
 */
@Data
public class Role {


    /**
     * 主键id
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;

    /**
     * 排序优先级
     */
    private Integer seq;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 1 表示删除，0 表示未删除
     */
    @JsonIgnore
    private Integer isDeleted;


}
