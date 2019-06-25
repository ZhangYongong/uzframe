package com.ughen.service.impl;

import com.github.pagehelper.PageHelper;
import com.ughen.constants.Constants;
import com.ughen.constants.ResultCode;
import com.ughen.mapper.ResourceMapper;
import com.ughen.model.db.Resource;
import com.ughen.service.ResourceService;
import com.ughen.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceMapper resourceDao;

    @Autowired
    private com.ughen.util.redisutils.RedisCommand redisUtils;

    @Override
    public Resource get(Long id) {
        return resourceDao.get(id);
    }

    @Override
    public List<Resource> list(Resource resource) {
        return resourceDao.list(resource);
    }


    @Override
    public List<Resource> listPage(Resource resource, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Resource> list = resourceDao.list(resource);
        return list;
    }

    @Override
    public Tree<TreeData> getTree() {
        List<Tree<TreeData>> trees = new ArrayList<Tree<TreeData>>();
        Resource resource1 = new Resource();
        resource1.setIsDeleted(0);
        List<Resource> list = resourceDao.list(resource1);
        for (Resource resource : list) {
            Tree<TreeData> tree = new Tree<TreeData>();
            tree.setText(resource.getName());
            tree.setParentId(resource.getPid() + "");
            tree.setValue(resource.getId().toString());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<TreeData> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public JsonResult save(Resource resource) {
        if (StringUtil.isEmpty(resource.getName()) || StringUtil.isEmpty(resource.getPid()) ||
                StringUtil.isEmpty(resource.getOpened())
                || StringUtil.isEmpty(resource.getSeq()) || StringUtil.isEmpty(resource.getResourceType())) {
            return JsonResult.build(ResultCode.MISSING_PARAM);
        }
        resource.setCreateTime(new Date());
        resource.setIsDeleted(0);
        int i = resourceDao.save(resource);
        if (i > 0) {
            redisUtils.remove(Constants.CLOSE_RESOURCES_KEY);
            return JsonResult.build(ResultCode.SUCCESS);
        }
        return JsonResult.build(ResultCode.ERROR);

    }

    @Override
    public JsonResult update(Resource resource) {
        if (StringUtil.isEmpty(resource.getId()) || StringUtil.isEmpty(resource.getName()) || StringUtil.isEmpty(resource.getPid()) ||
                StringUtil.isEmpty(resource.getDescription()) || StringUtil.isEmpty(resource.getOpened())
                || StringUtil.isEmpty(resource.getSeq()) || StringUtil.isEmpty(resource.getUrl())) {
            return JsonResult.build(ResultCode.MISSING_PARAM);
        }
        int i = resourceDao.update(resource);
        if (i > 0) {
            redisUtils.remove(Constants.CLOSE_RESOURCES_KEY);
            return JsonResult.build(ResultCode.SUCCESS);
        }
        return JsonResult.build(ResultCode.ERROR);

    }

    @Override
    public JsonResult remove(Long id) {
        if (StringUtil.isEmpty(id)) {
            return JsonResult.build(ResultCode.MISSING_PARAM);
        }
        int i = resourceDao.remove(id);
        if (i > 0) {
            redisUtils.remove(Constants.CLOSE_RESOURCES_KEY);
            return JsonResult.build(ResultCode.SUCCESS);
        }
        return JsonResult.build(ResultCode.ERROR);
    }

    @Override
    public JsonResult batchRemove(Long[] ids) {
        int i = resourceDao.batchRemove(ids);
        if (i > 0) {
            redisUtils.remove(Constants.CLOSE_RESOURCES_KEY);
            return JsonResult.build(ResultCode.SUCCESS);
        }
        return JsonResult.build(ResultCode.SUCCESS);
    }

    @Override
    public List<String> selectCloseResource() {
        return resourceDao.selectCloseResource();
    }

}
