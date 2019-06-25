package com.ughen.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.ughen.constants.QiNiuZone;
import com.ughen.exception.ConfigException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-22
 * Time: 15:50
 */
@Component
@ConfigurationProperties(prefix = "spring.qiniuyun")
public class QiNiuConfig {

    private String path;
    private String accessKey;
    private String secretKey;
    private String zone;
    private Configuration configuration;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Configuration getConfiguration() throws ConfigException {
        Zone zone;
        QiNiuZone qiNiuZone = QiNiuZone.fromQiNiuZoneName(this.zone);
        switch (qiNiuZone) {
            case z0:
                zone = Zone.zone0();
                break;
            case z1:
                zone = Zone.zone1();
                break;
            case z2:
                zone = Zone.zone2();
                break;
            case na0:
                zone = Zone.zoneNa0();
                break;
            case as0:
                zone = Zone.zoneAs0();
                break;
            default:
                throw new ConfigException("Zone对象配置错误！");

        }
        this.configuration = new Configuration(zone);
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
