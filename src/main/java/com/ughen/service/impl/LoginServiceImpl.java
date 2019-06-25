package com.ughen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.ughen.constants.Constants;
import com.ughen.constants.ResultCode;
import com.ughen.jwt.token.UserToken;
import com.ughen.jwt.token.UserTokenUtils;
import com.ughen.mapper.LoginMapper;
import com.ughen.model.db.Role;
import com.ughen.model.db.ThirdInfo;
import com.ughen.model.db.User;
import com.ughen.service.LoginService;
import com.ughen.service.UserRoleService;
import com.ughen.util.HttpUtils;
import com.ughen.util.JsonResult;
import com.ughen.util.Md5Encrypt;
import com.ughen.util.redisutils.RedisCommand;
import com.ughen.util.VerifyCodeUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 登录实现
 *
 * @author feifei
 */
@Service
@Log
public class LoginServiceImpl implements LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    protected RedisCommand redisUtils;

    /**
     * 注册用户
     */
    @Override
    public JsonResult register(User user) {
        ResultCode r = ResultCode.INSTER_USER_ERROR;
        //密码加盐
        String password = addSalt(user.getPassword());
        user.setPassword(password);
        user.setNickname(VerifyCodeUtils.verifyCode(10));
        try {
            loginMapper.insert(user);
            r = ResultCode.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("LoginServiceImpl | register | 用户新增失败", e);
        }
        return JsonResult.build(r);
    }

    /**
     * 密码加盐加密
     *
     * @param password 密码
     */
    @Override
    public String addSalt(String password) {
        StringBuilder salt = new StringBuilder(Constants.SALT);
        salt.append(password);
        return Md5Encrypt.md5(salt.toString());
    }

    /**
     * 查询用户信息
     */
    @Override
    public JsonResult userExistInfo(User user) {
        user = getUser(user);
        if (user != null) {
            return JsonResult.build(ResultCode.EXIST_USER);
        }
        return JsonResult.build(ResultCode.SUCCESS);
    }

    /**
     * 登录时查询用户
     */
    @Override
    public User login(User user) {
        return getUser(user);
    }

    public User getUser(User user) {
        return loginMapper.select(user);
    }

    /**
     * 第三方登录没有用户时，走手机短信注册用户
     */
    @Override
    public void smsRegister(User u) {
        loginMapper.insert(u);
    }

    /**
     * 根据第三方uid获取用户信息
     */
    @Override
    public User getUserByThirdUid(Map map) {
        return loginMapper.getUserByThirdUid(map);
    }

    /**
     * 通过手机获取用户信息
     */
    @Override
    public User getUserByMobile(User user) {
        return getUser(user);
    }

    /**
     * 查询第三方信息是否已绑定用户
     */
    @Override
    public JsonResult getThird(String mobile, Long id, String code) {
        Map map = getRedisThirdInfo(code);
        if (null == map) {
            return JsonResult.build(ResultCode.THIRD_CODE_FAILURE);
        }
        String source = map.get("source").toString();
        source = Constants.LOGIN_ALI.equalsIgnoreCase(source) ? Constants.LOGIN_ALIPAY
                : Constants.LOGIN_SINA.equalsIgnoreCase(source) ? Constants.LOGIN_SINAWEIBO
                        : Constants.LOGIN_QQ;
        map.put("userId", id);
        map.remove("uid");
        //根据userId与source查询此同一第三方是否已绑定平台账号
        ThirdInfo third = loginMapper.getThird(map);
        map.remove("userId");
        map.clear();
        if (null != third) {
            third = new ThirdInfo();
            //第三方来源ali,sina
            third.setSource(source);
            //手机已注册
            third.setRegister(Constants.YES);
            //已关联第三方其它账号，询问用户是否继续，继续则修改第三方信息为本次第三方账号的信息
            third.setBindThird(Constants.YES);
            //给用户的提示信息
            third.setMsg("该手机已绑定" + source + "第三方账号，是否重新绑定为本次第三方账号");
        } else {
            third = new ThirdInfo();
            //手机已注册
            third.setRegister(Constants.YES);
            //未关联则直接绑定本次第三方账号信息
            third.setBindThird(Constants.NO);
        }
        return JsonResult.build(ResultCode.SUCCESS, third);
    }

    /**
     * 从redis中获取第三方信息
     */
    @Override
    public Map getRedisThirdInfo(String code) {
        JSONObject jsonObject = null;
        try {
            Object o = redisUtils.get(code);
            jsonObject = JSONObject.parseObject(o.toString());
        } catch (Exception e) {
            logger.error("LoginServiceIml | getRedisThirdInfo | redis无法连接，可能未开启", e);
        }
        return (Map) jsonObject;
    }

    /**
     * 第三方登录--注册用户
     *
     * @param user 用户 信息
     * @param map 第三方信息
     */
    @Override
    public User addUser(User user, Map map) {
        user.setPassword(Md5Encrypt.md5(user.getPassword()));
        user.setNickname(VerifyCodeUtils.verifyCode(10));
        //新增用户
        loginMapper.insert(user);
        //新增第三方信息
        map.put("userId", user.getUserId());
        loginMapper.addThird(map);

        return resultUserInfo(user, map);
    }

    /**
     * 新增  用户与第三方信息的绑定
     */
    @Override
    public User addBinding(User user, Map map) {
        //新增第三方信息
        map.put("userId", user.getUserId());
        loginMapper.addThird(map);
        return resultUserInfo(user, map);
    }

    /**
     * 重新绑定  用户的第三方信息
     */
    @Override
    public User rebinding(User user, Map map) {
        map.put("userId", user.getUserId());
        //修改第三方信息
        loginMapper.updateThird(map);
        //向redis新增token
        return resultUserInfo(user, map);
    }

    /**
     * 第三方登录新增用户 或  绑定已有账号----返回用户信息
     */
    private User resultUserInfo(User user, Map map) {
        String deviceNoIn = user.getDeviceNo();
        String accessToken = map.get("accessToken").toString();
        String source = map.get("source").toString();
        /**
         * app应用使用第三方登录时将token存入redis
         */
        setTokenToRedis(deviceNoIn, accessToken, user, source);
        user.setAccessToken(map.get("accessToken").toString());

        return JSON.parseObject(JSON.toJSONString(user), user.getClass());
    }

    /**
     * 将用户的token存入redis中
     */
    @Override
    public void setTokenToRedis(String deviceNoIn, String accessToken, User user, String source)
            throws RuntimeException {
        /**
         * 查询用户权限
         */
        UserToken userToken = new UserToken();
        List<Role> roles = userRoleService.selectRoleById(user.getUserId());
        userToken.setRoles(roles);
        userToken.setUserId(user.getUserId());
        userToken.setAccessToken(accessToken);
        /**
         * redis目录层级存入
         */
        String platform = user.getPlatform();
        String userId = user.getUserId().toString();
        String userKey = UserTokenUtils.getUserKey(deviceNoIn, platform,userId);
        try {
            redisUtils.set(userKey, userToken, Constants.LOGIN_EXPIRE_TIME);
        } catch (Exception e) {
            logger.error("loginServiceImpl | setTokenToRedis | redis异常，可能未启动", e);
            throw new RuntimeException(e);
        }
    }


    @Value("${ali.appId}")
    private String appId;
    @Value("${ali.scope}")
    private String scope;
    @Value("${ali.privateKey}")
    private String privateKey;
    @Value("${ali.aliPublicKey}")
    private String aliPublicKey;
    @Value("${ali.url}")
    private String aliUrl;

    @Value("${sina.clientId}")
    private String clientId;
    @Value("${sina.clientSecret}")
    private String clientSecret;
    @Value("${sina.redirectUri}")
    private String redirectUri;
    @Value("${sina.reqUrl}")
    private String reqUrl;

    @Value("${qq.appId}")
    private String qqAppId;
    @Value("${qq.url}")
    private String url;

    /**
     * 获取qq用户信息中的openId
     */
    @Override
    public JsonResult getQQInfo(String accessToken) {
        try {
            String result = HttpUtils
                    .sendGet("https://graph.qq.com/oauth2.0/me", "access_token=" + accessToken,
                            null);
            Map<String, Object> resp = parseQQAuthResponse(result);
            String openId = (String) resp.get("openid");
            return JsonResult.build(ResultCode.SUCCESS, openId);
        } catch (Exception e) {
            return JsonResult.build(ResultCode.NETWORKEXCEPTION);
        }
        /**
         * 获取qq的个人信息
         */
//        result = HttpUtils.sendGet("https://graph.qq.com/user/get_user_info",
//                "access_token=" + accessToken + "&oauth_consumer_key=" + qqAppId + "&openid="
//                        + openId);
//        Map<String, Object> map = parseQQAuthResponse(result);
//        Integer z = new Integer(map.get("ret").toString());
        /**
         * 没能获取到qq的个人信息
         */
//        if (z != 0) {
//            JsonResult jr = new JsonResult();
//            jr.setCode("404");
//            jr.setMsg("获取个人信息失败");
//            return jr;
//        }
//        String nickname = map.get("nickname").toString();
    }

    /**
     * qq第三方登录--这个方法就是把结果转Map
     */
    public Map<String, Object> parseQQAuthResponse(String s) {
        String callBack = "callback";
        if (s.contains(callBack)) {
            s = s.substring(s.indexOf(callBack+"(") + 9, s.lastIndexOf(")"));
        }
        System.out.println(s);
        Map<String, Object> map = (Map<String, Object>) JSON.parse(s);
        return map;
    }

    /**
     * 第三方登录
     */
    @Override
    public JsonResult thirdLogin(String source, String code, String accessToken, String deviceNoIn,
            String uid) {
        Map map = new HashMap(16);
        map.put("uid", uid);
        /**
         * 根据第三方用户id查询平台用户
         */
        User user = getUserByThirdUid(map);

        try {
            /**
             * 判断用户是否存在，并返回用户信息
             */
            user = judgeUser(source, code, accessToken, deviceNoIn, user, uid);
        } catch (Exception e) {
            return JsonResult.build(ResultCode.NETWORKEXCEPTION);
        }
        return JsonResult.build(ResultCode.SUCCESS, user);
    }


    /**
     * 通过新浪的code获取token和uid，并返回uid
     */
    @Override
    public JsonResult getSinaToken(String code) {
        try {
            /**
             * 通过code拿到新浪的token 和 uid
             */
            Map map = new HashMap();
            map.put("client_id", clientId);
            map.put("client_secret", clientSecret);
            map.put("grant_type", "authorization_code");
            map.put("redirect_uri", redirectUri);
            map.put("code", code);
            String s = HttpUtils.postUrlEncodeForm(reqUrl, map, null);
            if (null == s) {
                return JsonResult.build(ResultCode.NETWORKEXCEPTION);
            }
            /**
             * 解析数据
             */
            JSONObject jsonObject = JSONObject.parseObject(s);
            Object uid = jsonObject.get("uid");
            return JsonResult.build(ResultCode.SUCCESS, uid);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.build(ResultCode.NETWORKEXCEPTION);
        }
    }


    /**
     * 通过code获取ali的token，再获取用户信息并返回 用户信息。
     */
    @Override
    public JsonResult getAliToken(String code) throws AlipayApiException {
        /**
         * 通过code拿到支付宝的token
         */
        AlipayClient alipayClient = new DefaultAlipayClient(aliUrl, appId, privateKey, "json",
                "UTF-8", aliPublicKey, "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(code);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        String data = s.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        request.setRefreshToken(data + uuid);

        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        if (!response.isSuccess()) {
            return JsonResult.build(ResultCode.ERROR, response.getSubMsg());
        }
        /**
         * 通过token获取用户的信息
         */
        AlipayUserInfoShareRequest userRequest = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse userInfo = alipayClient
                .execute(userRequest, response.getAccessToken());
        if (!userInfo.isSuccess()) {
            return JsonResult.build(ResultCode.ERROR, userInfo.getSubMsg());
        }
        return JsonResult.build(ResultCode.SUCCESS, userInfo);
    }

    /**
     * 第三方登录--ali回调路径
     */
    @Override
    public String getAliUrl() {
        return "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + appId
                + "&scope=" + scope + "&redirect_uri=" + redirectUri;
    }

    /**
     * 三方登录--sina回调路径
     */
    @Override
    public String getSinaUrl() {
        return "https://api.weibo.com/oauth2/authorize?client_id=" + clientId
                + "&response_type=code&redirect_uri=" + redirectUri;
    }

    /**
     * 三方登录--qq回调路径
     */
    @Override
    public String getQQUrl() throws UnsupportedEncodingException {
        String state = UUID.randomUUID().toString().trim().replace("-", "");
        JsonResult jr = JsonResult.build(ResultCode.SUCCESS);
        String rUrl =
                url + "?response_type=token&client_id=" + qqAppId + "&redirect_uri=" + URLEncoder
                        .encode(redirectUri, "UTF-8") + "&state=" + state;
        return rUrl;
    }


    /**
     * 第三方登录--判断本平台用户信息
     */
    public User judgeUser(String source, String code, String accessToken, String deviceNoIn,
            User user, String uid) {
        if (null != user) {
            /**
             * 如果有存入redis并直接返回用户信息
             */
            user.setAccessToken(accessToken);
            setTokenToRedis(deviceNoIn, accessToken, user, source);
        } else {
            /**
             * 第三方未绑定用户则返回token为空，前台则调用短信校验 （checkThird）
             */
            try {
                /**
                 * 将第三方用户信息存入到rdis中
                 */
                Map resultMap = new HashMap();
                resultMap.put("accessToken", accessToken);
                resultMap.put("uid", uid);
                resultMap.put("source", source);
                redisUtils.set(code, JSONObject.toJSONString(resultMap),
                        Constants.THIRD_USER_INFO_EXPIRE_TIME);
            } catch (Exception e) {
                logger.error("loginServiceImpl | setRedis | redis异常，可能未开启", e);
                throw new RuntimeException(e);
            }
            user = new User();
            user.setAccessToken(null);
            user.setSource(source);
        }
        return user;
    }


}
