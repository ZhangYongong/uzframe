package com.ughen.util;


import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Yonghong Zhang
 * @Date: 14:05 2019/1/28
 */
public class BuildTree {
    public static <T> Tree<T> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
        for (Tree<T> children : nodes) {
            String pid = children.getParentId();
            if (StringUtil.isEmpty(pid) || "0".equals(pid)) {
                topNodes.add(children);
                continue;
            }
            for (Tree<T> parent : nodes) {
                String id = parent.getValue();
                if (id != null && id.equals(pid)) {
                    parent.getChilds().add(children);
                    continue;
                }
            }
        }
        Tree<T> root = new Tree<T>();
        //如果只有一条数也放childs中
        /*if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setParentId("");
            root.setText("");
            root.setChilds(topNodes);
        }*/
        root.setParentId("");
        root.setText("");
        root.setChilds(topNodes);

        return root;
    }

    public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();

        for (Tree<T> children : nodes) {

            String pid = children.getParentId();
            if (pid == null || idParam.equals(pid)) {
                topNodes.add(children);

                continue;
            }

            for (Tree<T> parent : nodes) {
                String id = parent.getValue();
                if (id != null && id.equals(pid)) {
                    parent.getChilds().add(children);
                    continue;
                }
            }

        }
        return topNodes;
    }

}
