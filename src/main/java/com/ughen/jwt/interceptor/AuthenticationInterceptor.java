package com.ughen.jwt.interceptor;

import com.alibaba.fastjson.JSON;
import com.ughen.constants.Constants;
import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.PassToken;
import com.ughen.jwt.annotation.UserLoginToken;
import com.ughen.jwt.token.UserToken;
import com.ughen.jwt.token.UserTokenUtils;
import com.ughen.model.db.Role;
import com.ughen.service.ResourceService;
import com.ughen.service.RoleResourceService;
import com.ughen.util.JsonResult;
import com.ughen.util.redisutils.RedisCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;


/**
 * @Author:Yonghong Zhang
 * @Date: 14:19 2019/1/29
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RedisCommand redisUtils;

    @Autowired
    private ResourceService resourceService;

    @Value("${ughen.interceptor}")
    private Boolean isInterceptor;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws IOException {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        String requestURI = httpServletRequest.getRequestURI();
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //验证已关闭资源
        if (!checkCloseResources(httpServletResponse, requestURI)) {
            return false;
        }
        //检查有没有需要用户权限的注解或默认拦截
        if (method.isAnnotationPresent(UserLoginToken.class) || isInterceptor) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            String userKey;
            if (userLoginToken.required()) {
                // 获取 token 中的 user id
                userKey = UserTokenUtils.getUserKey(httpServletRequest);
                //验证资源
                return checkOpenResources(httpServletResponse, requestURI, userKey);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object
            o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse
            httpServletResponse, Object o, Exception e) throws Exception {
    }


    /**
     * 验证关闭资源
     *
     * @param httpServletResponse
     * @param requestURI
     * @return
     * @throws IOException
     */
    public Boolean checkCloseResources(HttpServletResponse httpServletResponse, String requestURI) throws IOException {
        List<String> closeResources = (List<String>) redisUtils.get(Constants.CLOSE_RESOURCES_KEY);
        if (closeResources == null) {
            closeResources = resourceService.selectCloseResource();
            redisUtils.set(Constants.CLOSE_RESOURCES_KEY, closeResources, Constants.USER_ROLE_EXPIRE_TIME);
        }
//        redisUtils.setExpireTime(Constants.CLOSE_RESOURCES_KEY, Constants.USER_ROLE_EXPIRE_TIME);
        if (closeResources.contains(requestURI)) {
            JsonResult build = JsonResult.build(ResultCode.CLOSE_RESOURCES);
            httpServletResponse.getWriter().write(JSON.toJSONString(build));
            return false;
        }
        return true;
    }

    /**
     * 验证限制资源
     *
     * @param httpServletResponse
     * @param requestURI
     * @return
     * @throws IOException
     */
    public Boolean checkOpenResources(HttpServletResponse httpServletResponse, String requestURI, String userKey) throws IOException {
        UserToken user = (UserToken) redisUtils.get(userKey);
        if (user == null) {
            JsonResult build = JsonResult.build(ResultCode.TOKEN_EXPIRE);
            httpServletResponse.getWriter().write(JSON.toJSONString(build));
            return false;
        }
        redisUtils.setExpireTime(userKey, Constants.LOGIN_EXPIRE_TIME);
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            List<String> strings = (List<String>) redisUtils.get("role:id:" + role.getId());
            if (strings == null) {
                strings = roleResourceService.selectUrlsByRoleId(role.getId());
                redisUtils.set("role:id:" + role.getId(), strings, Constants.USER_ROLE_EXPIRE_TIME);
            }
            redisUtils.setExpireTime("role:id:" + role.getId(), Constants.USER_ROLE_EXPIRE_TIME);
            if (strings.contains(requestURI)) {
                return true;
            }
        }
        JsonResult build = JsonResult.build(ResultCode.NO_PERMISSION);
        httpServletResponse.getWriter().write(JSON.toJSONString(build));
        return false;
    }

}