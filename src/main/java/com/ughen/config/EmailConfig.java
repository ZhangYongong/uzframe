package com.ughen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-22
 * Time: 17:39
 */
@Component
public class EmailConfig {
    @Value("${email.smtp.host}")
    private String host;
    @Value("${email.send.from}")
    private String from;
    @Value("${email.user.name}")
    private String name;
    @Value("${email.user.password}")
    private String password;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
