package com.ughen.model.db;

import lombok.Data;

/**
 * 用户角色
 *
 * @Author:Yonghong Zhang
 * @Date: 11:22 2019/1/28
 */
@Data
public class UserRole {

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;

}
