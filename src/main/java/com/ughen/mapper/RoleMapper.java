package com.ughen.mapper;

import com.ughen.model.db.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色
 *
 * @author Yonghong Zhang
 * @date 2019-01-29 16:38:40
 */
@Mapper
public interface RoleMapper {

    Role get(Long id);

    List<Role> list(Role role);

    int save(Role role);

    int update(Role role);

    int remove(Long id);

    int batchRemove(Long[] ids);



}
