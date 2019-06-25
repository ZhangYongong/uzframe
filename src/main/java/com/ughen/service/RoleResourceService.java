package com.ughen.service;

import com.ughen.model.db.Role;
import com.ughen.model.db.RoleResource;
import com.ughen.util.JsonResult;
import com.ughen.util.Tree;
import com.ughen.util.TreeData;

import java.util.List;

/**
 * 角色资源
 *
 * @author Yonghong Zhang
 * @date 2019-01-29 16:38:38
 */
public interface RoleResourceService {

    RoleResource get(Long id);

    List<RoleResource> list(RoleResource roleResource);

    int save(RoleResource roleResource);

    int update(RoleResource roleResource);

    int remove(Long id);

    int batchRemove(Long[] ids);


    /**
     * 根据角色id修改资源访问权限
     *
     * @param roleId
     * @param resourceIds
     */
    void updateResourceByRoleId(Long roleId, List<Long> resourceIds);


    /**
     * 根据角色id查询访问资源
     *
     * @param roleId
     * @return
     */
    List<String> selectUrlsByRoleId(Long roleId);


    /**
     * 获取当前角色菜单
     *
     * @param roles
     * @return
     */
    Tree<TreeData> getMenu(List<Role> roles);


    /**
     * 根据角色id查询访问资源id
     *
     * @param roleId
     * @return
     */
    JsonResult getResourceIdsByRoleId(Long roleId);

}
