package com.ughen.service.impl;

import com.ughen.constants.ResultCode;
import com.ughen.mapper.RoleResourceMapper;
import com.ughen.model.db.Resource;
import com.ughen.model.db.Role;
import com.ughen.model.db.RoleResource;
import com.ughen.service.RoleResourceService;
import com.ughen.util.*;
import com.ughen.util.redisutils.RedisCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RoleResourceServiceImpl implements RoleResourceService {


    @Autowired
    private RedisCommand redisUtils;

    @Autowired
    private RoleResourceMapper roleResourceDao;

    @Override
    public RoleResource get(Long id) {
        return roleResourceDao.get(id);
    }

    @Override
    public List<RoleResource> list(RoleResource roleResource) {
        return roleResourceDao.list(roleResource);
    }

    @Override
    public int save(RoleResource roleResource) {
        return roleResourceDao.save(roleResource);
    }

    @Override
    public int update(RoleResource roleResource) {
        return roleResourceDao.update(roleResource);
    }

    @Override
    public int remove(Long id) {
        return roleResourceDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return roleResourceDao.batchRemove(ids);
    }


    @Override
    public void updateResourceByRoleId(Long roleId, List<Long> resourceIds) {
        roleResourceDao.deleteByRoleId(roleId);
        redisUtils.remove("role:id:" + roleId);
        List<RoleResource> list = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            RoleResource userRole = new RoleResource();
            userRole.setRoleId(roleId);
            userRole.setResourceId(resourceId);
            list.add(userRole);
        }
        if (list.size() > 0) {
            roleResourceDao.insertRoleResources(list);
        }
    }

    @Override
    public List<String> selectUrlsByRoleId(Long roleId) {
        List<String> strings = roleResourceDao.selectByUserId(roleId);
        return strings;
    }

    @Override
    public Tree<TreeData> getMenu(List<Role> roles) {
        List<Long> list = new ArrayList<>();
        for (Role role : roles) {
            list.add(role.getId());
        }
        if (list.size() == 0) {
            return null;
        }
        List<Resource> resources = roleResourceDao.selectResourceByRoleIds(list);
        List<Tree<TreeData>> trees = new ArrayList<Tree<TreeData>>();
        for (Resource resource : resources) {
            Tree<TreeData> tree = new Tree<TreeData>();
            tree.setText(resource.getName());
            tree.setParentId(resource.getPid() + "");
            tree.setValue(resource.getId().toString());
            trees.add(tree);
        }
        // 默认顶级菜单为0，根据数据库实际情况调整
        Tree<TreeData> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public JsonResult getResourceIdsByRoleId(Long roleId) {
        if (StringUtil.isEmpty(roleId)) {
            return JsonResult.build(ResultCode.MISSING_PARAM);
        }
        List<Long> ids = roleResourceDao.selectResourceIdsByRoleId(roleId);
        return JsonResult.build(ResultCode.SUCCESS, ids);
    }

}
