package com.ughen.service;

import com.alipay.api.AlipayApiException;
import com.ughen.model.db.User;
import com.ughen.util.JsonResult;
import io.lettuce.core.RedisException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author feifei
 */
public interface LoginService {

  JsonResult register(User user);

  JsonResult userExistInfo(User user);

  User login(User user);

  void smsRegister(User u);

  User getUserByThirdUid(Map map);

  User getUserByMobile(User user);

  JsonResult getThird(String mobile, Long id, String code);

  User addUser(User user, Map map);

  Map getRedisThirdInfo(String code);

  User addBinding(User user, Map map);

  User rebinding(User user, Map map);

  void setTokenToRedis(String deviceNoIn, String accessToken, User user, String source)
          throws RuntimeException;

  JsonResult getSinaToken(String code);

  JsonResult getAliToken(String code) throws AlipayApiException;

  String getAliUrl();

  String getSinaUrl();

  String getQQUrl() throws UnsupportedEncodingException;

  JsonResult getQQInfo(String accessToken);

  JsonResult thirdLogin(String source, String code, String accessToken, String deviceNoIn, String uid);

    String addSalt(String password);
}
