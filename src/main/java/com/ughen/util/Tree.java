package com.ughen.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2018-11-09
 * Time: 17:19
 */
public class Tree<T> {
    /**
     * 节点ID
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private String value;
    /**
     * 显示节点文本
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private String text;

    /**
     * 节点的子节点
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private List<Tree<T>> childs = new ArrayList<Tree<T>>();

    /**
     * 父ID
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private String parentId;

    /**
     * 级别
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private String level;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public List<Tree<T>> getChilds() {
        return childs;
    }

    public void setChilds(List<Tree<T>> childs) {
        this.childs = childs;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Tree() {
        super();
    }

    @Override
    public String toString() {

        return JSON.toJSONString(this);
    }

}
