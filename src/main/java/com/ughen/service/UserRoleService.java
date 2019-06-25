package com.ughen.service;


import com.ughen.model.db.Role;
import com.ughen.model.db.UserRole;

import java.util.List;

/**
 * 用户角色
 *
 * @author Yonghong Zhang
 * @date 2019-01-29 16:38:35
 */
public interface UserRoleService {

    UserRole get(Long id);

    List<UserRole> list(UserRole userRole);

    int save(UserRole userRole);

    int update(UserRole userRole);

    int remove(Long id);

    int batchRemove(Long[] ids);

    /**
     * 根据用户id查询资源访问权限
     *
     * @param userId
     * @return
     */
    List<String> selectResourceUrlById(Long userId);

    /**
     * 根据用户id查询资源访问权限
     *
     * @param userId
     * @return
     */
    List<Role> selectRoleById(Long userId);

    /**
     * 根据用户id修改角色
     *
     * @param userId
     * @param roleIds
     */
    void updateRoleByUserId(Long userId, List<Long> roleIds);
}
