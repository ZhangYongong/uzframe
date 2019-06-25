package com.ughen.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Yonghong Zhang
 * @Date: 2019-02-13
 * @Time: 18:20
 */
@Data
public class ResourceByRoleVO {

    private Long roleId;
    private List<Long> resourceIds;
}
