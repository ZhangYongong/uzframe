package com.ughen.service;

import com.ughen.model.db.Role;
import com.ughen.model.vo.RolePageVO;
import com.ughen.util.JsonResult;

import java.util.List;

/**
 * 角色
 *
 * @author Yonghong Zhang
 * @date 2019-01-29 16:38:40
 */
public interface RoleService {

    Role get(Long id);

    List<Role> list(Role role);

    /**
     * 查询角色列表（分页）
     *
     * @param rolePage
     * @return
     */
    JsonResult listPage(RolePageVO rolePage);


    JsonResult save(Role role);

    JsonResult update(Role role);

    JsonResult remove(Long id);

    JsonResult batchRemove(Long[] ids);
}
