package com.ughen.controller;

import com.ughen.constants.ResultCode;
import com.ughen.jwt.annotation.PassToken;
import com.ughen.model.db.VerifyCode;
import com.ughen.util.JsonResult;
import com.ughen.util.StringUtil;
import com.ughen.util.VerifyCodeUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生成与校验验证码
 *
 * @author
 */
@RestController
public class VerifyCodeController {

    /**
     * 获取验证码 json返回
     */
    @PassToken
    @GetMapping("/getStrVerify")
    public JsonResult getStrVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        //制作验证码
        String code = VerifyCodeUtils.verifyCode(6);
        //将验证码塞入session中
        VerifyCodeUtils.setCode(request, code);
        return JsonResult.build(ResultCode.SUCCESS, code);
    }

    /**
     * 获取图片验证码，流返回
     */
    @PassToken
    @GetMapping("/getStreamCode")
    public void getStreamCode(HttpServletRequest request, HttpServletResponse response) {
        //制作验证码图片
        VerifyCode verifyCode = VerifyCodeUtils.makeVerifyCode(request, response);
        //将验证码塞入session中
        VerifyCodeUtils.setCode(request, verifyCode.getCode());
        //返回流验证码
        VerifyCodeUtils.outputStream(response, verifyCode.getBfi());
    }

    /**
     * 校验验证码(json与流可共用)
     */
    @PassToken
    @GetMapping("/checkVerifyCode")
    public JsonResult checkVerifyCode(HttpServletRequest request, HttpServletResponse response,
            @Param("code") String code) {
        ResultCode resultCode = ResultCode.VERIFY_ERROR;
        if (StringUtil.isEmpty(code)) {
            resultCode = ResultCode.MISSING_PARAM;
        } else {
            /**
             * 拿取session中验证码
             */
            Object sessionVerify = request.getSession().getAttribute("checkCode");
            if (sessionVerify != null) {
                /**
                 * 比较验证码是否相同 不区分大小写（将两个值都转换成小写再进行比较）
                 */
                if ((code.toLowerCase()).equals(sessionVerify.toString().toLowerCase())) {
                    resultCode = ResultCode.SUCCESS;
                }
            } else {
                resultCode = ResultCode.SMS_CODE_RESTART;
            }
        }
        return JsonResult.build(resultCode);
    }
}
