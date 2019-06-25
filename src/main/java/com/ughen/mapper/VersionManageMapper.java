package com.ughen.mapper;

import com.ughen.model.db.VersionManage;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VersionManageMapper {

    List<VersionManage> getVersionManage(@Param("versionSource") String versionSource,@Param("versionNum") String versionNum);
}

