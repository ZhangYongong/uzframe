package com.ughen.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.ughen.constants.Constants;
import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.PassToken;
import com.ughen.jwt.token.UserTokenUtils;
import com.ughen.model.db.ThirdInfo;
import com.ughen.model.db.User;
import com.ughen.service.LoginService;
import com.ughen.util.JsonResult;
import com.ughen.util.TokenUtils;
import com.ughen.util.VerifyCodeUtils;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 * @author feifei
 */
@RestController
@RequestMapping("/user/")
public class LoginController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 4。第三方登录新增用户 或  绑定已有账号
     */
    @PostMapping("third/user/add")
    @PassToken
    public JsonResult thirdAddUser(HttpServletRequest request, HttpServletResponse response,
            @RequestBody User user) {
        getDeviceAndPlatformFromHeader(request, user);
        /**
         * 校验必入参娄
         */
        if (StringUtils.isEmpty(user.getMobile()) || StringUtils.isEmpty(user.getPlatform())
                || StringUtils.isEmpty(user.getDeviceNo()) || StringUtils.isEmpty(user.getCode())
                || StringUtils.isEmpty(user.getBindThird())) {
            return JsonResult
                    .build(ResultCode.MISSING_PARAM, "mobile/platform/deviceNo/code/bindThird");
        }
        //根据电话查询此电话是否已注册
        User getUser = loginService.getUserByMobile(user);
        /**
         * get third-party  user  information  from redis
         */
        Map map = loginService.getRedisThirdInfo(user.getCode());
        if (null == map) {
            return JsonResult.build(ResultCode.THIRD_CODE_FAILURE);
        }

        User resultUser = null;
        if (null == getUser) {
            //新增用户
            if (StringUtils.isEmpty(user.getPassword())) {
                return JsonResult.build(ResultCode.MISSING_PARAM, "password");
            }
            resultUser = loginService.addUser(user, map);
        } else {//将此token绑定到这个电话的用户上
            user.setUserId(getUser.getUserId());
            user.setHeadImgPath(getUser.getHeadImgPath());
            user.setNickname(getUser.getNickname());

            if (Constants.NO.equals(user.getBindThird())) {
                resultUser = loginService.addBinding(user, map);
            } else {
                //同一第三方不同账号绑定一个平台用户
                resultUser = loginService.rebinding(user, map);
            }
        }
        return JsonResult.build(ResultCode.SUCCESS, resultUser);
    }

    /**
     * 3。手机校验 第三方登录accessToken为null时，调用此方法 。 根据电话查询此电话是否已注册,第三方是否已关联过平台用户 如果未注册过，肯定是未关联过第三方账号
     * 如果注册过，有可能关联过第三方账号
     * <p>
     * 之后 调用 third/user/add 方法
     */
    @GetMapping("third/mobile/check")
    @PassToken
    public JsonResult checkThird(HttpServletRequest request, @Param("mobile") String mobile,
            @Param("code") String code) {
        String platform = request.getHeader("platform");
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code) || StringUtils
                .isEmpty(platform)) {
            return JsonResult.build(ResultCode.MISSING_PARAM, "mobile/code/platform");
        }
        /**
         * 根据电话查询此电话是否已注册
         */
        User user = new User();
        user.setMobile(mobile);
        user.setPlatform(platform);
        user = loginService.getUserByMobile(user);
        //未注册，页面显示密码输入框--注册用户
        ThirdInfo thirdInfo = new ThirdInfo();
        if (null == user) {
            /**
             * 未注册用户，直接发送短信  并显示密码框让用户注册
             */
            thirdInfo.setRegister(Constants.NO);
            //ps:短信码的验证有单独的公共接口
        } else {
            /**
             * 已注册，页面不显示密码输入框，并返回是否已关联同一第三方其它账号
             * 查询是否已关联同一第三方其它账号  bindThird  为yse是关联过，为no是未关联过
             */
            return loginService.getThird(mobile, user.getUserId(), code);

        }
        return JsonResult.build(ResultCode.SUCCESS, thirdInfo);
    }

    /**
     * 1。第三方获取回调code
     */
    @GetMapping("thirdLogin/getCode")
    @PassToken
    public JsonResult getThirdCode(@Param("thirdSource") Integer thirdSource) {
        String url = "";
        switch (thirdSource) {
            //阿里
            case 1:
                url = loginService.getAliUrl();
                break;
            //新浪
            case 2:
                url = loginService.getSinaUrl();
                break;
            //qq
            case 3:
                try {
                    url = loginService.getQQUrl();
                } catch (Exception e) {
                    return JsonResult.build(ResultCode.NETWORKEXCEPTION);
                }
                break;
            default:
                return JsonResult.build(ResultCode.THIRD_SOURCE_ERROR);
        }
        return JsonResult.build(ResultCode.SUCCESS, url);
    }

    /**
     * 2。第三方登录,如果返回出来的accessToken为null，则需要走手机校验流程-- third/mobile/check --此方法 ；不为空，则直接跳登录
     */
    @PassToken
    @GetMapping("third/login")
    public JsonResult thirdLogin(HttpServletRequest request, HttpServletResponse response,
            @Param("code") String code, @Param("thirdSource") Integer thirdSource,
            @Param("accessToken") String accessToken) throws IOException {
        String deviceNo = request.getHeader("deviceNo");
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(deviceNo)) {
            return JsonResult.build(ResultCode.MISSING_PARAM, "code/deviceNo");
        }
        switch (thirdSource) {
            case 1:
                accessToken = TokenUtils.makeAccessToken();
                return thirdAliLogin(code, accessToken, deviceNo);
            case 2:
                accessToken = TokenUtils.makeAccessToken();
                return thirdSinaLogin(code, accessToken, deviceNo);
            case 3:
                return thirdQqLogin(response, code, accessToken, deviceNo);
            default:
                return JsonResult.build(ResultCode.THIRD_SOURCE_ERROR);
        }
    }

    /**
     * 第三方qq登录
     */
    private JsonResult thirdQqLogin(HttpServletResponse response, @Param("code") String code,
            @Param("accessToken") String accessToken, String deviceNoIn) throws IOException {
        /**
         * 重定向回到这里  由于 qq 的返回值 accessToken 之前加了#号 所以需要重定向
         */
        if (accessToken == null) {
            String html =
                    "<!DOCTYPE html>" + "<html lang=\"zh-cn\">" + "<head>" + "   <title></title>"
                            + "   <meta charset=\"utf-8\"/>" + "</head>" + "<body>"
                            + "   <script type=\"text/javascript\">"
                            + "   location.href = location.href.replace('#', '').replace('auth_qq', 'auth_qq_redirect');"
                            + "   </script>" + "</body>" + "</html>";
            response.getWriter().print(html);
            return null;
        }
        /**
         * 获取qq的openId
         */
        JsonResult jr = loginService.getQQInfo(accessToken);
        if (jr.getCode() != ResultCode.SUCCESS.getCode()) {
            return jr;
        }
        String source = Constants.LOGIN_QQ;
        String uid = (String) jr.getData();

        return loginService.thirdLogin(source, code, accessToken, deviceNoIn, uid);
    }

    /**
     * 第三方新浪登录
     */
    private JsonResult thirdSinaLogin(@Param("code") String code,
            @Param("accessToken") String accessToken, String deviceNoIn) {
        /**
         * 通过code拿到新浪的token 和 uid
         */
        JsonResult jr = loginService.getSinaToken(code);
        if (jr.getCode() != ResultCode.SUCCESS.getCode()) {
            return jr;
        }
        String source = Constants.LOGIN_SINA;
        String uid = (String) jr.getData();
        /**
         * 根据uid查询表中是否有用户数据
         */
        return loginService.thirdLogin(source, code, accessToken, deviceNoIn, uid);
    }

    /**
     * 第三方阿里登录
     */
    private JsonResult thirdAliLogin(@Param("code") String code,
            @Param("accessToken") String accessToken, String deviceNoIn) {
        AlipayUserInfoShareResponse userInfo = null;
        try {
            /**
             * 通过code拿到支付宝的用户信息
             */
            JsonResult jr = loginService.getAliToken(code);
            if (jr.getCode() != ResultCode.SUCCESS.getCode()) {
                return jr;
            }
            userInfo = (AlipayUserInfoShareResponse) jr.getData();
        } catch (Exception e) {
            logger.error("loginController | thirdLogin | 阿里获取用户信息异常", e);
            return JsonResult.build(ResultCode.NETWORKEXCEPTION);
        }
        String source = Constants.LOGIN_ALI;
        String uid = userInfo.getUserId();

        /**
         * 根据第三方用户id查询表中是否有用户数据
         */
        return loginService.thirdLogin(source, code, accessToken, deviceNoIn, uid);
    }

    /**
     * 退出
     */
    @PassToken
    @DeleteMapping("loginOut")
    public JsonResult loginOut(HttpServletRequest request) {
        String platform = request.getHeader("platform");
        String deviceNo = request.getHeader("deviceNo");
        String userId = request.getHeader("userId");
        if (StringUtils.isEmpty(deviceNo) || StringUtils.isEmpty(platform) || StringUtils
                .isEmpty(userId)) {
            return JsonResult.build(ResultCode.MISSING_PARAM, "platform/userId/deviceNo");
        }
        /**
         * 获取用户的key
         */
        String userKey = UserTokenUtils.getUserKey(deviceNo, platform, userId);
        redisUtils.remove(userKey);
        return JsonResult.build(ResultCode.SUCCESS);
    }

    /**
     * 手机短信验证码登录
     */
    @PostMapping("smsLogin")
    @PassToken
    public JsonResult smsLogin(HttpServletRequest request, @RequestBody User user) {
        getDeviceAndPlatformFromHeader(request, user);
        /**
         * 校验必入参娄
         */
        if (StringUtils.isEmpty(user.getMobile()) || StringUtils.isEmpty(user.getSmsCode())
                || StringUtils.isEmpty(user.getPlatform()) || StringUtils
                .isEmpty(user.getDeviceNo())) {
            return JsonResult.build(ResultCode.MISSING_PARAM, "mobile/smsCode/platform/deviceNo");
        }
        /**
         * 从redis中获取验证码
         */
        Object smsCodeRedis = "";
        /**
         * redis拿到的验证码与传入的一样
         */
        if (smsCodeRedis != null && user.getSmsCode().equals(smsCodeRedis.toString())) {
            String accessToken = TokenUtils.makeAccessToken();
            String mobile = user.getMobile();
            String deviceNoIn = user.getDeviceNo();
            String platform = user.getPlatform();
            /**
             * 查询用户信息
             */
            user = loginService.login(user);
            try {
                if (user != null) {
                    /**
                     * 将token与用户信息存入redis
                     */
                    user = userSetRedis(deviceNoIn, accessToken, user);
                } else {
                    /**
                     * 注册一个用户,此用户没有密码，需要在平台提供的地方录入，不录入则只能短信登录
                     */
                    String nickname = VerifyCodeUtils.verifyCode(10);
                    user = new User();
                    user.setNickname(nickname);
                    user.setMobile(mobile);
                    user.setPlatform(platform);
                    loginService.smsRegister(user);
                    user = userSetRedis(deviceNoIn, accessToken, user);
                }
            } catch (Exception e) {
                logger.error("LoginController | smsLogin | redis异常，可能未启动", e);
                return JsonResult.build(ResultCode.NETWORKEXCEPTION);
            }
            return JsonResult.build(ResultCode.SUCCESS, user);

        } else {
            return JsonResult.build(ResultCode.VERIFY_ERROR);
        }
    }

    /**
     * 将token与用户信息存入redis
     */
    private User userSetRedis(String deviceNoIn, String accessToken, User user) {
        user.setAccessToken(accessToken);
        user.setDeviceNo(deviceNoIn);
        try {
            loginService.setTokenToRedis(deviceNoIn, accessToken, user, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    /**
     * 账号密码登录
     */
    @PassToken
    @PostMapping("login")
    public JsonResult login(HttpServletRequest request, @RequestBody User user) {
        /**
         * 随机生成的token
         */
        String accessToken = TokenUtils.makeAccessToken();
        /**
         * 判断用户名是否为空--三者必需有一个
         */
        if (!StringUtils.isEmpty(user.getMobile()) || !StringUtils.isEmpty(user.getMail())
                || !StringUtils.isEmpty(user.getUsername())) {
            /**
             * 入参密码是否为空
             */
            String password = user.getPassword();
            if (password == null) {
                return JsonResult.build(ResultCode.MISSING_PARAM, "password");
            }
            /**
             * 查询用户信息
             */
            user.setPlatform(request.getHeader("platform"));
            user = loginService.login(user);
            /**
             * 用户为空
             */
            if (user == null) {
                return JsonResult.build(ResultCode.VALIDATION_ERROR);
            }
            /**
             * 密码错误
             */
            password = loginService.addSalt(password);
            if (!password.equals(user.getPassword())) {
                return JsonResult.build(ResultCode.VALIDATION_ERROR);
            }
            /**
             * 返回用户信息
             */
            /**
             * 入参设备
             */
            String deviceNoIn = request.getHeader("deviceNo");
            try {
                user = userSetRedis(deviceNoIn, accessToken, user);
            } catch (Exception e) {
                logger.error("LoginController | login | redis异常，可能未启动", e);
                return JsonResult.build(ResultCode.NETWORKEXCEPTION);
            }
            return JsonResult.build(ResultCode.SUCCESS,
                    JSON.parseObject(JSON.toJSONString(user), user.getClass()));
        }
        return JsonResult.build(ResultCode.MISSING_PARAM, "mobile/mail/username");
    }

    /**
     * 用户注册
     */
    @PassToken
    @PostMapping("register")
    public JsonResult register(HttpServletRequest request, @RequestBody User user) {
        getDeviceAndPlatformFromHeader(request, user);
        /**
         * 校验必入参娄
         */
        if (StringUtils.isEmpty(user.getMobile()) || StringUtils.isEmpty(user.getTwoPassword())
                || StringUtils.isEmpty(user.getPassword()) || StringUtils
                .isEmpty(user.getPlatform()) || StringUtils.isEmpty(user.getDeviceNo())) {
            return JsonResult.build(ResultCode.MISSING_PARAM,
                    "mobile/password/twoPassword/platform/deviceNo");
        }
        /**
         * 校验两次密码是否一样
         */
        if (!user.getPassword().equals(user.getTwoPassword())) {
            return JsonResult.build(ResultCode.NOT_SAME_PASSWORD);
        }
        return loginService.register(user);
    }

    private void getDeviceAndPlatformFromHeader(HttpServletRequest request,
            @RequestBody User user) {
        String deviceNo = request.getHeader("deviceNo");
        String platform = request.getHeader("platform");
        user.setPlatform(platform);
        user.setDeviceNo(deviceNo);
    }

    /**
     * 检查用户是否存在
     */
    @GetMapping("userExistInfo")
    @PassToken
    public JsonResult userExistInfo(@RequestBody User user) {
        //判断该用户是否已经存在
        return loginService.userExistInfo(user);
    }

}
