package com.ughen.util.alisms;

/**
 * 短信模板(模板需要修改)
 *
 * @author feifei
 */

public enum SmsTemplate {
    /**
     * 注册
     */
    Register("SMS_150174698", "注册", false, new String[]{"code"}),
    /**
     * 找回密码
     */
    FindPassword("SMS_150174723", "找回密码", false, new String[]{"code"});

    /**
     *商家同意退款（多个参数示例）
     */
    //	SellerAgressRefund("SMS_8670", "发货", false, new String[] {"goodsName", "orderId"});

    /**
     * 模块名称
     */
    private String templateId;
    /**
     * 描述
     */
    private String describe;
    /**
     * 是否多发
     */
    private Boolean isGroupSend;
    private String[] parameterKey;

    private SmsTemplate(String templateId, String describe, Boolean isGroupSend,
            String[] parameterKey) {
        this.templateId = templateId;
        this.describe = describe;
        this.isGroupSend = isGroupSend;
        this.parameterKey = parameterKey;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getDescribe() {
        return describe;
    }

    public Boolean getIsGroupSend() {
        return isGroupSend;
    }

    public String[] getParameterKey() {
        return parameterKey;
    }
}
