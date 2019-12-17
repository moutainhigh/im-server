package org.chl.login;

import akka.actor.ActorSystem;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@MapperScan("org.chl.db.data.mapper")
@SpringBootApplication(scanBasePackages = {"org.chl"})
public class LoginServerApplication{

    @Autowired
    private ActorSystem actorSystem;

    public static void main(String[] args) {
        SpringApplication.run(LoginServerApplication.class, args);
    }
}
