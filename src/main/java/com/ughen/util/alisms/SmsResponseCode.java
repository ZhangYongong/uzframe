package com.ughen.util.alisms;

/**
 * 短信返回信息
 * @author feifei
 */

public enum SmsResponseCode {
	/**
	 * 发送成功
	 */
	OK("OK", "发送成功", "发送成功"),
	OUT_OF_SERVICE("isv.OUT_OF_SERVICE", "业务停机", "业务停机"),
	PRODUCT_UNSUBSCRIBE("isv.PRODUCT_UNSUBSCRIBE", "产品服务未开通", "产品服务未开通"),
	ACCOUNT_NOT_EXISTS("isv.ACCOUNT_NOT_EXISTS", "账户信息不存在", "系统异常"),
	ACCOUNT_ABNORMAL("isv.ACCOUNT_ABNORMAL", "账户信息异常", "系统异常"),
	SMS_TEMPLATE_ILLEGAL("isv.SMS_TEMPLATE_ILLEGAL", "模板不合法", "模板不合法"),
	SMS_SIGNATURE_ILLEGAL("isv.SMS_SIGNATURE_ILLEGAL", "签名不合法", "签名不合法"),
	MOBILE_NUMBER_ILLEGAL("isv.MOBILE_NUMBER_ILLEGAL", "手机号码格式错误", "手机号码格式错误"),
	MOBILE_COUNT_OVER_LIMIT("isv.MOBILE_COUNT_OVER_LIMIT", "手机号码数量超过限制", "手机号码数量超过限制"),
	TEMPLATE_MISSING_PARAMETERS("isv.TEMPLATE_MISSING_PARAMETERS", "短信模板变量缺少参数", "短信模板变量缺少参数"),
	INVALID_PARAMETERS("isv.INVALID_PARAMETERS", "参数异常", "参数异常"),
	/*
	 * 短信验证码，使用同一个签名，对同一个手机号码发送短信验证码，支持1条/分钟，5条/小时，10条/天。
	 * 一个手机号码通过阿里大于平台只能收到40条/天。 
	 * 短信通知，使用同一签名、同一模板，对同一手机号发送短信通知，允许每天50条（自然日）。
	 */
	BUSINESS_LIMIT_CONTROL("isv.BUSINESS_LIMIT_CONTROL", "触发业务流控限制", "短信业务繁忙，请稍后重试"),
	INVALID_JSON_PARAM("isv.INVALID_JSON_PARAM", "JSON参数不合法", "JSON参数不合法"),
	SYSTEM_ERROR("isp.SYSTEM_ERROR", "业务停机", "业务停机"),
	
	/**
	 * 如：阿里大鱼	黑名单关键字禁止在模板变量中使用，若业务确实需要使用，建议将关键字放到模板中，进行审核。
	 */
	BLACK_KEY_CONTROL_LIMIT("isv.BLACK_KEY_CONTROL_LIMIT", "模板变量中存在黑名单关键字", "模板变量中存在黑名单关键字"),
	
	/**
	 * 域名和ip请固化到模板申请中
	 */
	PARAM_NOT_SUPPORT_URL("isv.PARAM_NOT_SUPPORT_URL", "不支持url为变量", "不支持url为变量"),
	
	/**
	 * 请尽量固化变量中固定部分
	 */
	PARAM_LENGTH_LIMIT("isv.PARAM_LENGTH_LIMIT", "变量长度受限", "变量长度受限"),
	
	/**
	 * 因余额不足未能发送成功，请登录管理中心充值后重新发送
	 */
	AMOUNT_NOT_ENOUGH("isv.AMOUNT_NOT_ENOUGH", "余额不足", "余额不足");
	
	private String code;
	private String innerMsg;
	private String outMsg;
	
	private SmsResponseCode(String code, String innerMsg, String outMsg) {
		this.code = code;
		this.innerMsg = innerMsg;
		this.outMsg = outMsg;
	}

	/**
	 * 返回code
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public String getInnerMsg() {
		return innerMsg;
	}

	public String getOutMsg() {
		return outMsg;
	}
}
