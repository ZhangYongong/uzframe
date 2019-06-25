package com.ughen.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.ughen.constants.Constants;
import com.ughen.constants.ResultCode;
import com.ughen.service.AliSmsService;
import com.ughen.util.NumberUtils;
import com.ughen.util.redisutils.RedisCommand;
import com.ughen.util.alisms.SmsData;
import com.ughen.util.alisms.SmsManager;
import com.ughen.util.alisms.SmsResponseCode;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 短信实现
 * @author feifei
 */
@Service
public class AliSmsServiceImpl implements AliSmsService {

    private static Logger logger = LoggerFactory.getLogger(AliSmsServiceImpl.class);

    @Autowired
    protected RedisCommand redisUtils;

    /**
     * 发送短信
     * @param mobile
     * @param data
     * @return
     */
    @Override
    public ResultCode sendMsg(String mobile, SmsData data) {
        ResultCode r = ResultCode.NETWORKEXCEPTION;
        try {
            String code = NumberUtils.randomInt(Constants.ALI_SMS_CODE_LENGTH);
            Map<String, String> map = new HashMap();
            map.put("code", code);
            data.setMsgData(map);
            data.setPhoneNumbers(mobile);
            SmsResponseCode smsCode = SmsManager.sendSms(data);
            //判断阿里返回的code
            r = judgeAliResponseCode(mobile, r, code, smsCode);
        } catch (ClientException e) {
            logger.error("AliSmsServiceImpl | sendMsg | 阿里短信发送异常", e);
        } catch (Exception e) {
            logger.error("AliSmsServiceImpl | sendMsg | 短信发送失败", e);
        }
        return r;
    }

    /**
     * 判断阿里返回的code
     * @param mobile
     * @param r
     * @param code
     * @param smsCode
     * @return
     */
    private ResultCode judgeAliResponseCode(String mobile, ResultCode r, String code,
            SmsResponseCode smsCode) {
        switch (smsCode) {
            case OK:
                r = ResultCode.SUCCESS;
                // 将验证码加入到redis缓存
                try {
                    redisUtils.set(mobile, code, Constants.SMS_CODE_EXPIRE_TIME);
                } catch (Exception e) {
                    logger.error("AliSmsServiceImpl | sendMsg | redis异常，可能未开启 ", e);
                }
                break;
            case BUSINESS_LIMIT_CONTROL:
                /**
                 * 短信发送频繁
                 */
                r = ResultCode.SMS_BUSINESS_LIMIT_CONTROL;
                break;
            case SYSTEM_ERROR:
                logger.error("AliSmsServiceImpl | sendMsg | 业务停机或余额不足" + smsCode.getOutMsg());
                break;
            case AMOUNT_NOT_ENOUGH:
                logger.error("AliSmsServiceImpl | sendMsg | 业务停机或余额不足" + smsCode.getOutMsg());
                break;
            default:
                logger.error("AliSmsServiceImpl | sendMsg | 其它异常" + smsCode.getOutMsg());
                break;
        }
        return r;
    }

    /**
     * 校验短信验证码
     * @param mobile
     * @param smsCode
     * @return
     */
    @Override
    public ResultCode validateSmsCode(String mobile, String smsCode) {
        ResultCode r = ResultCode.VERIFY_ERROR;
        Object o = null;
        try {
            o = redisUtils.get(mobile);
            if (StringUtils.isEmpty(o)) {
                /**
                 * 重新获取验证码
                 */
                r = ResultCode.SMS_CODE_RESTART;
            } else if (smsCode.equals(o.toString())) {
                r = ResultCode.SUCCESS;
            } else if (Constants.OVERDUE.equals(o.toString())) {
                r = ResultCode.SMS_CODE_OVERDUE;
            }
        } catch (Exception e) {
            logger.error("AliSmsController | validateMessageCode | redis异常，可能未开启", e);
            r = ResultCode.NETWORKEXCEPTION;
        }
        return r;
    }
}
