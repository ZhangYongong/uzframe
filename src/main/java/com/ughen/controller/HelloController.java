package com.ughen.controller;

import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.UserLoginToken;
import com.ughen.util.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:Yonghong Zhang
 * @Date: 15:38 2019/1/30
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @UserLoginToken
    @ResponseBody
    public JsonResult hello() {
        return JsonResult.build(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult hello2() {
        return JsonResult.build(ResultCode.SUCCESS);
    }
}
