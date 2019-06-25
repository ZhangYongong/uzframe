package com.ughen.mapper;

import com.ughen.model.db.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 资源
 *
 * @author Yonghong Zhang
 * @date 2019-01-29 16:38:44
 */
@Mapper
public interface ResourceMapper {

    Resource get(Long id);

    List<Resource> list(Resource resource);

    int save(Resource resource);

    int update(Resource resource);

    int remove(Long id);

    int batchRemove(Long[] ids);

    /**
     * 查询关闭资源
     *
     * @return
     */
    List<String> selectCloseResource();
}
