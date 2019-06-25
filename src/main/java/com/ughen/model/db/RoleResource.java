package com.ughen.model.db;


import lombok.Data;

/**
 * 角色资源
 *
 * @Author:Yonghong Zhang
 * @Date: 11:18 2019/1/28
 */
@Data
public class RoleResource {

    private Long id;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色id
     */
    private Long resourceId;

}
