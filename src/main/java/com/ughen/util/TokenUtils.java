package com.ughen.util;

import java.util.UUID;

/**
 * token工具类
 *
 * @author feifei
 */
public class TokenUtils {

    /**
     * 生成token
     *
     * @return
     */
    public static String makeAccessToken() {
        return System.currentTimeMillis() + "" + UUID.randomUUID().toString().replaceAll("-", "");
    }

}
