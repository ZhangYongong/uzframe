package com.ughen.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ughen.constants.ResultCode;
import com.ughen.mapper.RoleMapper;
import com.ughen.model.db.PageBean;
import com.ughen.model.db.Role;
import com.ughen.model.vo.RolePageVO;
import com.ughen.service.RoleService;
import com.ughen.util.JsonResult;
import com.ughen.util.StringUtil;
import com.ughen.util.redisutils.RedisCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleDao;

    @Autowired
    private RedisCommand redisUtils;

    @Override
    public Role get(Long id) {
        return roleDao.get(id);
    }

    @Override
    public List<Role> list(Role role) {
        return roleDao.list(role);
    }

    @Override
    public JsonResult listPage(RolePageVO rolePage) {
        if (StringUtil.isEmpty(rolePage) || StringUtil.isEmpty(rolePage.getStart())
                || StringUtil.isEmpty(rolePage.getLength())) {
            return JsonResult.build(ResultCode.MISSING_PARAM);
        }
        int pageSize = Integer.parseInt(rolePage.getLength());
        int pageNum = (Integer.parseInt(rolePage.getStart()) / pageSize) + 1;
        Role role = new Role();
        role.setDescription(rolePage.getDescription());
        role.setName(rolePage.getName());
        PageHelper.startPage(pageNum, pageSize);
        List<Role> list = roleDao.list(role);
        PageInfo<Role> pageInfo = new PageInfo(list);
        PageBean pageBean = new PageBean(pageNum, pageSize, (int) pageInfo.getTotal(), list);
        return JsonResult.build(ResultCode.SUCCESS, pageBean);

    }

    @Override
    public JsonResult save(Role role) {
        role.setIsDeleted(0);
        int i = roleDao.save(role);
        if (i > 0) {
            return JsonResult.build(ResultCode.SUCCESS);
        }
        return JsonResult.build(ResultCode.ERROR);
    }

    @Override
    public JsonResult update(Role role) {
        int i = roleDao.update(role);
        if (i > 0) {
            return JsonResult.build(ResultCode.SUCCESS);
        }
        return JsonResult.build(ResultCode.ERROR);

    }

    @Override
    public JsonResult remove(Long id) {
        redisUtils.remove("role:id:" + id);
        int i = roleDao.remove(id);
        if (i > 0) {
            return JsonResult.build(ResultCode.SUCCESS);
        }
        return JsonResult.build(ResultCode.ERROR);
    }

    @Override
    public JsonResult batchRemove(Long[] ids) {
        for (Long id : ids) {
            redisUtils.remove("role:id:" + id);
        }
        int i = roleDao.batchRemove(ids);
        if (i > 0) {
            return JsonResult.build(ResultCode.SUCCESS);
        }
        return JsonResult.build(ResultCode.ERROR);
    }

}
