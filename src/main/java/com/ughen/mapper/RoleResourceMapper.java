package com.ughen.mapper;

import com.ughen.model.db.Resource;
import com.ughen.model.db.RoleResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色资源
 *
 * @author Yonghong Zhang
 * @date 2019-01-29 16:38:38
 */
@Mapper
public interface RoleResourceMapper {

    RoleResource get(Long id);

    List<RoleResource> list(RoleResource roleResource);

    int save(RoleResource roleResource);

    int update(RoleResource roleResource);

    int remove(Long id);

    int batchRemove(Long[] ids);


    /**
     * 根据角色id集合查询可访问资源集合集合
     *
     * @param roleIds
     * @return
     */
    List<String> selectByUserIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据roleId删除数据
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量新增
     *
     * @param list
     * @return
     */
    int insertRoleResources(@Param("list") List<RoleResource> list);

    /**
     * 根据角色id查询可访问资源集合集合
     *
     * @param roleId
     * @return
     */
    List<String> selectByUserId(@Param("roleId") Long roleId);


    /**
     * 根据角色id集合查询可访问资源集合集合
     *
     * @param roleIds
     * @return
     */
    List<Resource> selectResourceByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据角色id集合查询可访问资源集合集合
     *
     * @param roleId
     * @return
     */
    List<Long> selectResourceIdsByRoleId(@Param("roleId") Long roleId);
}
