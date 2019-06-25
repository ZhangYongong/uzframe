package com.ughen.controller;

import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.UserLoginToken;
import com.ughen.model.db.Resource;
import com.ughen.model.vo.ResourceByRoleVO;
import com.ughen.service.ResourceService;
import com.ughen.service.RoleResourceService;
import com.ughen.util.JsonResult;
import com.ughen.util.Tree;
import com.ughen.util.TreeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author:Yonghong Zhang
 * @Date: 14:29 2019/1/30
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleResourceService roleResourceService;

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult getAll(@RequestBody Resource resource) {
        List<Resource> roles = resourceService.list(resource);
        return JsonResult.build(ResultCode.SUCCESS, roles);
    }


    /**
     * 获取资源树结构
     */
    @RequestMapping(value = "/getTree", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult getTree() {
        Tree<TreeData> tree = resourceService.getTree();
        return JsonResult.build(ResultCode.SUCCESS, tree);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    public JsonResult save(@RequestBody Resource resource) {
        return resourceService.save(resource);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    public JsonResult del(Long resourceId) {
        return resourceService.remove(resourceId);
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    public JsonResult update(@RequestBody Resource resource) {
        return resourceService.update(resource);
    }


    /**
     * 根据角色id获取资源id集合
     *
     * @return
     */
    @RequestMapping(value = "/getResourceIdsByRoleId", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult getResourceIdsByRoleId(@RequestBody ResourceByRoleVO resourceByRoleVO) {
        return roleResourceService.getResourceIdsByRoleId(resourceByRoleVO.getRoleId());
    }
}
