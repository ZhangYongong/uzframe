package com.ughen.model.db;

import lombok.Data;

import java.util.Date;

/**
 * 访问资源
 *
 * @Author:Yonghong Zhang
 * @Date: 11:08 2019/1/28
 */
@Data
public class Resource {


    /**
     * 主键id
     */
    private Long id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源路径
     */
    private String url;

    /**
     * 资源介绍
     */
    private String description;

    /**
     * 父级资源id
     */
    private Long pid;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 状态 1 表示删除,0 表示未删除
     */
    private Integer isDeleted;

    /**
     * 打开状态 1 表示关闭,0 表示打开
     */
    private Integer opened;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 资源类别 1.菜单资源 2.接口资源
     */
    private Integer resourceType;
}
