package com.ughen.util;

import lombok.Data;

/**
 * @Author:Yonghong Zhang
 * @Date: 14:48 2019/1/30
 */
@Data
public class TreeData {
    //值
    private String value;
    //上级id
    private String parentId;
    //名称
    private String label;
    //级别
    private String level;

}
