package com.ughen.controller;

import com.ughen.jwt.token.UserToken;
import com.ughen.jwt.token.UserTokenUtils;
import com.ughen.util.redisutils.RedisCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-30
 * Time: 15:07
 */
public class BaseController {

//    @InitBinder
//    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        CustomDateEditor dateEditor = new CustomDateEditor(format, true);
//        binder.registerCustomEditor(Date.class, dateEditor);
//    }

    @Autowired
    protected RedisCommand redisUtils;


    public Object getAttribute(String attributeName) {
        return this.getRequest().getAttribute(attributeName);
    }

    public void setAttribute(String attributeName, Object object) {
        this.getRequest().setAttribute(attributeName, object);
    }

    public Object getSession(String attributeName) {
        return this.getRequest().getSession(true).getAttribute(attributeName);
    }

    public void setSession(String attributeName, Object object) {
        this.getRequest().getSession(true).setAttribute(attributeName, object);
    }

    public HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getRequest();
    }

    public HttpServletResponse getResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getResponse();
    }

    public HttpSession getSession() {
        return this.getRequest().getSession(true);
    }

    public String getParameter(String paraName) {
        return this.getRequest().getParameter(paraName);
    }


    public String getHeader(String headerName) {
        return this.getRequest().getHeader(headerName);
    }

    /**
     * 获取访问用户的ip
     *
     * @return
     */
    public String getIpAddress() {
        String ip = this.getRequest().getRemoteAddr();
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取服务器ip地址
     *
     * @return
     */
    public String getServerIpAddress() {
        InetAddress address;
        String serverIpAddress = null;
        try {
            address = InetAddress.getLocalHost();
            serverIpAddress = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return serverIpAddress;
    }


    /**
     * 获取当前登录用户对象
     *
     * @return {UserToken}
     */
    public UserToken getUserToken() {
        String userKey = UserTokenUtils.getUserKey(getRequest());
        UserToken user = (UserToken) redisUtils.get(userKey);
        return user;
    }

    /**
     * 获取当前登录用户id
     *
     * @return {Long}
     */
    public Long getUserId() {
        return this.getUserToken().getUserId();
    }


}
