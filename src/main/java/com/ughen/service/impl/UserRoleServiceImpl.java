package com.ughen.service.impl;

import com.ughen.mapper.RoleResourceMapper;
import com.ughen.mapper.UserRoleMapper;
import com.ughen.model.db.Role;
import com.ughen.model.db.UserRole;
import com.ughen.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** 
 * @Description:  
 * @Author: Yonghong Zhang
 * @Date: 2019/2/1 
*/

@Service
public class UserRoleServiceImpl implements UserRoleService {


    @Autowired
    private UserRoleMapper userRoleDao;
    @Autowired
    private RoleResourceMapper roleResourceDao;

    @Override
    public UserRole get(Long id) {
        return userRoleDao.get(id);
    }

    @Override
    public List<UserRole> list(UserRole userRole) {
        return userRoleDao.list(userRole);
    }

    @Override
    public int save(UserRole userRole) {
        return userRoleDao.save(userRole);
    }

    @Override
    public int update(UserRole userRole) {
        return userRoleDao.update(userRole);
    }

    @Override
    public int remove(Long id) {
        return userRoleDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return userRoleDao.batchRemove(ids);
    }

    @Override
    public List<String> selectResourceUrlById(Long userId) {
        List<Long> list = userRoleDao.selectByUserId(userId);
        List<String> urls = new ArrayList<>();
        if (list.size() > 0) {
            urls = roleResourceDao.selectByUserIds(list);
        }
        return urls;
    }

    @Override
    public List<Role> selectRoleById(Long userId) {
        return userRoleDao.selectRoleByUserId(userId);
    }

    @Override
    public void updateRoleByUserId(Long userId, List<Long> roleIds) {
        userRoleDao.deleteByUserId(userId);
        List<UserRole> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            list.add(userRole);
        }
        if (list.size() > 0) {
            userRoleDao.insertUserRoles(list);
        }
    }

}
