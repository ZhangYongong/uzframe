package com.ughen.controller;

import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.PassToken;
import com.ughen.model.db.VersionManage;
import com.ughen.service.VersionManageService;
import com.ughen.util.JsonResult;
import com.ughen.util.StringUtil;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app版本管理
 *
 * @Author feifei
 */
@RestController
public class VersionManageController {

    @Autowired
    private VersionManageService versionManageService;

    /**
     * app版本对比
     *
     * @param versionSource 对应版本类型
     * @param versionNum 版本号
     */
    @PassToken
    @GetMapping(value = {"versionManage"})
    public JsonResult versionManage(@Param("versionSource") String versionSource,
            @Param("versionNum") String versionNum) {
        if (StringUtil.isEmpty(versionSource) || StringUtil.isEmpty(versionNum)) {
            return JsonResult.build(ResultCode.MISSING_PARAM, "versionSource/versionNum");
        }
        /**
         * 查询比此版本更新的版本集合
         */
        List<VersionManage> list = versionManageService.getNewVersion(versionSource, versionNum);
        if (list.size() == 0) {
            return JsonResult.build(ResultCode.VERSION_IS_NEW);
        }
        //需要更新,如果强制更新字段和>=1,更新到最新版本,/

        VersionManage versionManage = versionManageService.checkVersion(list);
        return JsonResult.build(ResultCode.SUCCESS, versionManage);
    }

}