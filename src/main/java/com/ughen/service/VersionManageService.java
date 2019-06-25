package com.ughen.service;

import com.ughen.constants.ResultCode;
import com.ughen.model.db.VersionManage;
import java.util.List;

public interface VersionManageService {

    List<VersionManage> getNewVersion(String versionSource,String versionNum);

    VersionManage checkVersion( List<VersionManage> list);
}
