package com.ughen.util.alisms;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信数据封装
 *
 * @author feifei
 */
public class SmsData {

    /**
     * template
     */
    private SmsTemplate msgTemplate;
    /**
     * send data
     */
    private Map<String, String> data;
    /**
     * Multiple phone Numbers are separated by commas
     */
    private String phoneNumbers;

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getTemplateId() {
        return msgTemplate.getTemplateId();
    }

    public SmsData(SmsTemplate msgTemplate) {
        this.msgTemplate = msgTemplate;
        data = new HashMap<String, String>();
    }

    public SmsData(SmsTemplate msgTemplate, Map<String, String> data, String phoneNumbers) {
        this.msgTemplate = msgTemplate;
        this.data = data;
        this.phoneNumbers = phoneNumbers;
    }

    public String[] getMsgKey() {
        return msgTemplate.getParameterKey();
    }

    public Boolean setMsgData(Map<String, String> map) {
        Boolean result = true;
        if (map.size() > 1) {
            data = map;
        } else {
            String[] key = msgTemplate.getParameterKey();
            if (key != null) {
                for (int i = 0; i < key.length; i++) {
                    String value = map.get(key[i]);
                    if (value != null && !value.equals("")) {
                        data.put(key[i], value);
                    } else {
                        result = false;
                        break;
                    }
                }
            }
            // 如果为false，则清除数据
            if (!result) {
                data.clear();
            }
        }
        return result;
    }

    public String getData() {
        String result = "{";
        for (String key : data.keySet()) {
            result += "\"" + key + "\":\"" + data.get(key) + "\"" + ",";
        }
        result += "}";
        return result;
    }

}
