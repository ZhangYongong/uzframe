package com.ughen.service;


import com.ughen.model.db.Resource;
import com.ughen.util.JsonResult;
import com.ughen.util.Tree;
import com.ughen.util.TreeData;

import java.util.List;

/**
 * 资源
 *
 * @author Yonghong Zhang
 * @date 2019-01-29 16:38:44
 */
public interface ResourceService {

    Resource get(Long id);

    List<Resource> list(Resource resource);

    /**
     * 查询资源列表（分页）
     *
     * @param resource
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Resource> listPage(Resource resource, int pageNum, int pageSize);

    /**
     * 获取资源树结构
     *
     * @return
     */
    Tree<TreeData> getTree();

    JsonResult save(Resource resource);

    JsonResult update(Resource resource);

    JsonResult remove(Long id);

    JsonResult batchRemove(Long[] ids);

    /**
     * 查询所有关闭资源
     *
     * @return
     */
    List<String> selectCloseResource();
}
