package com.ughen.jwt.token;

import com.ughen.constants.Constants;
import javax.servlet.http.HttpServletRequest;

public class UserTokenUtils {

    public static String getUserKey(HttpServletRequest request) {
        // 从 http 请求头中取出 accessToken
        String accessToken = request.getHeader("accessToken");
        // 从 http 请求头中取出 deviceNoIn
        String deviceNoIn = request.getHeader("deviceNo");
        // 从 http 请求头中取出 platform
        String platform = request.getHeader("platform");
        // 从 http 请求头中取出 userId
        String userId = request.getHeader("userId");
        return getUserKey(deviceNoIn, platform, userId);
    }

    public static String getUserKey(String deviceNoIn, String platform, String userId) {
        String userKey = "";
        if (deviceNoIn.contains(Constants.APP)) {
            /**
             * app使用用户id作为key,不同设备只会向同一个key中更新token,可以实现app一个设备登录
             */
            userKey = "login:" + platform + ":app:" + userId;
        } else {
            /**
             * pc,wap等浏览器直接使用token作为key，可以实现不同设备多页面登录
             */
            userKey = "login:" + platform + ":pc:" + userId + ":" + deviceNoIn;
        }
        /**
         * 获取 token 中的 user id
         */
        return userKey;
    }
}
