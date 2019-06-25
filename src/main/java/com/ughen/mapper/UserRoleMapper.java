package com.ughen.mapper;

import com.ughen.model.db.Role;
import com.ughen.model.db.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色
 *
 * @author Yonghong Zhang
 * @date 2019-01-29 16:38:35
 */
@Mapper
public interface UserRoleMapper {

    UserRole get(Long id);

    List<UserRole> list(UserRole userRole);

    int save(UserRole userRole);

    int update(UserRole userRole);

    int remove(Long id);

    int batchRemove(Long[] ids);


    /**
     * 根据用户id查询角色id集合
     *
     * @param userId
     * @return
     */
    List<Long> selectByUserId(@Param("userId") Long userId);



    List<Role> selectRoleByUserId(@Param("userId") Long userId);


    /**
     * 根据userId删除数据
     *
     * @param userId
     * @return
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 批量新增
     *
     * @param list
     * @return
     */
    int insertUserRoles(@Param("list") List<UserRole> list);
}
