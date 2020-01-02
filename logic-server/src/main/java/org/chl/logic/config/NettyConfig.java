package org.chl.logic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther: monster
 * @Date: 2019/12/11
 * @Description: TODO
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "netty", ignoreInvalidFields = true)
public class NettyConfig {
    private int port;
}
