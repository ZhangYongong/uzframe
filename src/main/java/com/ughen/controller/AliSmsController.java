package com.ughen.controller;

import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.PassToken;
import com.ughen.service.AliSmsService;
import com.ughen.util.JsonResult;
import com.ughen.util.StringUtil;
import com.ughen.util.alisms.SmsData;
import com.ughen.util.alisms.SmsTemplate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信功能
 * @author feifei
 */
@RequestMapping("/sms/")
@RestController
public class AliSmsController {

    @Autowired
    AliSmsService aliSmsService;

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号码
     * @param type 短信类型
     */
    @PassToken
    @GetMapping("sendMsg")
    public JsonResult messageSend(@Param("mobile") String mobile, @Param("type") Integer type) {
        SmsData data = null;
        switch (type) {
            case 1:
                //注册
                data = new SmsData(SmsTemplate.Register);
                break;
            case 2:
                //找回密码
                data = new SmsData(SmsTemplate.FindPassword);
                break;
            default:
                return JsonResult.build(ResultCode.SMS_TYPPER_ERROR);
        }

        ResultCode r = aliSmsService.sendMsg(mobile, data);
        return JsonResult.build(r);
    }


    /**
     * 后台验证短信验证码
     */
    @PassToken
    @GetMapping("checkCode")
    public JsonResult validateMessageCode(@Param("mobile") String mobile, @Param("smsCode") String smsCode) {
        if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(smsCode)) {
            return JsonResult.build(ResultCode.MISSING_PARAM, "mobile/smsCode");
        }
        ResultCode r = aliSmsService.validateSmsCode(mobile, smsCode);
        return JsonResult.build(r);
    }
}
