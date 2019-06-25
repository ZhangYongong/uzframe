package com.ughen.model.vo;

import lombok.Data;

/**
 * @Description: 角色查询
 * @Author: Yonghong Zhang
 * @Date: 2019-02-15
 * @Time: 10:21
 */
@Data
public class RolePageVO {

    private String start;
    private String length;

    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;
}
