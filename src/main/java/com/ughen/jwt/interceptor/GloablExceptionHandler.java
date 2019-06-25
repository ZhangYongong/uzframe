//package com.ughen.jwt.interceptor;
//
//import com.ughen.constants.ResultCode;
//import com.ughen.util.JsonResult;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * @Author:Yonghong Zhang
// * @Date: 15:21 2019/1/29
// */
//@ControllerAdvice
//public class GloablExceptionHandler {
//    @ResponseBody
//    @ExceptionHandler(Exception.class)
//    public Object handleException(Exception e) {
//        String msg = e.getMessage();
//        if (msg == null || msg.equals("")) {
//            msg = "服务器出错";
//        }
//        return JsonResult.build(ResultCode.NETWORKEXCEPTION, msg);
//    }
//}
