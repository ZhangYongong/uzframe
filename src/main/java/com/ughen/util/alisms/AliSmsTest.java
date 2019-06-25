package com.ughen.util.alisms;

import com.aliyuncs.exceptions.ClientException;
import java.util.HashMap;
import java.util.Map;

public class AliSmsTest {

    /**
     * TEST
     * @param args
     */
    public static void main(String[] args)
    {
        String phone = "15190411498";
        //String phone = "15190411111,15190411112
        String code = "123456";
        //调用模板
        SmsData data = new SmsData(SmsTemplate.Register);
        //放入数据
        Map<String, String> map = new HashMap<String, String>(16);
        map.put("orderId", code);
        map.put("goodsName", code);

        data.setMsgData(map);
        data.setPhoneNumbers(phone);
        //发短信
        SmsResponseCode smscode = null;
        try {
            smscode = SmsManager.sendSms(data);
            if(SmsResponseCode.OK.equals(smscode.getCode())){
                System.out.println("成功");
            }else{
                //查看返回错误信息
                String outMsg = smscode.getOutMsg();
                System.out.println(outMsg);
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
