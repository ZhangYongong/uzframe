package com.ughen.service;

import com.ughen.constants.ResultCode;
import com.ughen.util.alisms.SmsData;

public interface AliSmsService {

    ResultCode sendMsg(String mobile, SmsData data);

    ResultCode validateSmsCode(String mobile, String smsCode);
}
