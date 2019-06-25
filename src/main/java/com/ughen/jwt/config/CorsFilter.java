package com.ughen.jwt.config;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2018-11-23
 * Time: 17:29
 */
@Component
public class CorsFilter implements Filter {

    private static Logger logger = Logger.getLogger(CorsFilter.class.getName());

    /**
     * 预请求
     */
    public static final String OPTIONS = "OPTIONS";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        StringBuffer content = new StringBuffer("");
//        String line = null;
//        BufferedReader br = request.getReader();
//        while ((line = br.readLine()) != null) {
//            //line = new String(line.getBytes(), "utf-8");
//            content.append(line);
//        }
//        logger.info(content);
//        System.out.println("content" + content);
        // 指定允许其他域名访问
        String origin = request.getHeader("Origin");
        if (origin == null) {
            origin = request.getHeader("Referer");
        }
        response.setHeader("Access-Control-Allow-Origin", origin);            // 允许指定域访问跨域资源
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 响应类型
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS");
        // 响应头设置
        //response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, HaiYi-Access-Token");
        response.setHeader("Access-Control-Allow-Headers", "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization,SessionToken,Set-Cookie");

        response.setHeader("Access-Control-Max-Age", "3600");

        if (OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpStatus.SC_NO_CONTENT);
            return;
        }
        chain.doFilter(req, res);

    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
