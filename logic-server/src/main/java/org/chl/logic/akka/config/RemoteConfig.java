package org.chl.logic.akka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Auther: monster
 * @Date: 2019/12/9
 * @Description: TODO
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "akka.remote", ignoreInvalidFields = true)
public class RemoteConfig {
    private String ip;
    private String port;
}
