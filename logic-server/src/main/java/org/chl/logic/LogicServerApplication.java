package org.chl.logic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.chl.db.data.mapper")
@SpringBootApplication(scanBasePackages = {"org.chl"})
public class LogicServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogicServerApplication.class, args);
    }
}
