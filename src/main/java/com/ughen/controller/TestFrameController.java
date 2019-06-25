package com.ughen.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ughen.constants.Constants;
import com.ughen.util.DataLogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
@RequestMapping("v1/api/test")
public class TestFrameController {

    private static final Logger logger = LoggerFactory.getLogger(TestFrameController.class);

    @RequestMapping(value = "/hello")
    public String testHello(@RequestBody Map<String,Object> map){
        String name =  "";
        try{
            name = map.get("name").toString();
        }catch(NullPointerException e){
            logger.error("TestFrameController || testHello || error :缺少參數或參數錯誤 ");
        }

        logger.info("TestFrameController || testHello || params:"+map.toString());

        //數據埋點操作
        JsonObject jsonObject = new JsonParser().parse(new Gson().toJson(map)).getAsJsonObject();
        sendDatatoLog(Constants.CODE_UZ_FRAME_TYPE,jsonObject);

        //返回JSON數據
        return "hello "+name;
    }


    /**
     * 日志上报
     * @param mName
     * @param object
     */
    private void sendDatatoLog(String mName, Object object) {

        DataLogHelper dataLogHelper = DataLogHelper.getLogger(Constants.MSG_UZ_FRAME_TYPE);
        String logstr = dataLogHelper.log(mName,new Gson().toJson(object));
        System.out.println("日志内容:"+logstr);
        logger.info("TestFrameController || sendDatatoLog || the log message :", logstr);

    }

}
