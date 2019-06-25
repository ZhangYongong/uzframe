package com.ughen.controller;

import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.UserLoginToken;
import com.ughen.jwt.token.UserToken;
import com.ughen.model.db.Role;
import com.ughen.model.vo.ResourceByRoleVO;
import com.ughen.model.vo.RoleByUserVO;
import com.ughen.model.vo.RolePageVO;
import com.ughen.service.RoleResourceService;
import com.ughen.service.RoleService;
import com.ughen.service.UserRoleService;
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
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-30
 * Time: 11:47
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 获取全部角色
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult getAll(@RequestBody Role role) {
        List<Role> roles = roleService.list(role);
        return JsonResult.build(ResultCode.SUCCESS, roles);
    }

    /**
     * 获取分页
     */
    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult getPage(@RequestBody RolePageVO rolePage) {
        return roleService.listPage(rolePage);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult save(@RequestBody Role role) {
        return roleService.save(role);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult del(@RequestBody ResourceByRoleVO resourceByRoleVO) {
        return roleService.remove(resourceByRoleVO.getRoleId());
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult update(@RequestBody Role role) {
        return roleService.update(role);
    }

    /**
     * 根据角色id修改角色访问资源
     */
    @RequestMapping(value = "/updateResourceByRoleId", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult updateResourceByRoleId(@RequestBody ResourceByRoleVO resourceByRoleVO) {
        roleResourceService.updateResourceByRoleId(resourceByRoleVO.getRoleId(), resourceByRoleVO.getResourceIds());
        return JsonResult.build(ResultCode.SUCCESS);
    }

    /**
     * 根据用户id修改拥有角色
     */
    @RequestMapping(value = "/updateRoleByUserId", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult updateRoleByUserId(@RequestBody RoleByUserVO roleByUserVO) {
        userRoleService.updateRoleByUserId(roleByUserVO.getUserId(), roleByUserVO.getRoleIds());
        return JsonResult.build(ResultCode.SUCCESS);
    }


    /**
     * 获取菜单树结构
     *
     * @return
     */
    @RequestMapping(value = "/getMenu", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult getMenu() {
        UserToken userToken = getUserToken();
        List<Role> roles = userToken.getRoles();
        Tree<TreeData> menu = roleResourceService.getMenu(roles);
        return JsonResult.build(ResultCode.SUCCESS, menu);
    }


    /**
     * 根据用户id获取角色id集合
     *
     * @return
     */
    @RequestMapping(value = "/getRoleIdsByUserId", method = RequestMethod.POST)
    @UserLoginToken
    @ResponseBody
    public JsonResult getRoleIdsByUserId(@RequestBody RoleByUserVO roleByUserVO) {
        List<Role> roles = userRoleService.selectRoleById(roleByUserVO.getUserId());
        return JsonResult.build(ResultCode.SUCCESS, roles);
    }

}
