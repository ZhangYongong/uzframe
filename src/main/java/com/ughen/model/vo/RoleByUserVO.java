package com.ughen.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Yonghong Zhang
 * @Date: 2019-02-13
 * @Time: 18:05
 */
@Data
public class RoleByUserVO {

    private Long userId;
    private List<Long> roleIds;

}
