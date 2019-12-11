package org.chl.logic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther: monster
 * @Date: 2019/12/11
 * @Description: TODO
 */
@Configuration
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
@ConfigurationProperties(prefix = "netty", ignoreInvalidFields = true)
public class NettyConfig {

    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
