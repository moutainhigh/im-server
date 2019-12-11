package org.chl.login.akka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther: monster
 * @Date: 2019/12/9
 * @Description: TODO
 */
@Configuration
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
@ConfigurationProperties(prefix = "akka.remote", ignoreInvalidFields = true)
public class RemoteConfig {
    private String ip;
    private String port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
