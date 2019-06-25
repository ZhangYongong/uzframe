package com.ughen.service.impl;

import com.ughen.mapper.VersionManageMapper;
import com.ughen.model.db.VersionManage;
import com.ughen.service.VersionManageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author feifei
 */
@Service
public class VersionManageServiceImpl implements VersionManageService {

    @Autowired
    private VersionManageMapper versionManageMapper;

    /**
     * 查询大于此版本号的实体集合
     */
    @Override
    public List<VersionManage> getNewVersion(String versionSource, String versionNum) {
        return versionManageMapper.getVersionManage(versionSource, versionNum);
    }

    /**
     * 校验是否有强制更新版本
     * @param list
     * @return
     */
    @Override
    public VersionManage checkVersion(List<VersionManage> list) {
        int forceUpdate = 0;
        for (VersionManage v : list) {
            forceUpdate += v.getIsForceUpdate();
        }
        VersionManage entity = list.get(0);
        if (forceUpdate >= 1) {
            //版本中有强制更新,则不管最新版本是否为强制更新,都设置为强制更新
            entity.setIsForceUpdate(1);
        }
        return entity;
    }
}
