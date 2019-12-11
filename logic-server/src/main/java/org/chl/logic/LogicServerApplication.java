package org.chl.logic;

import org.chl.logic.config.NettyConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.chl.db.data.mapper")
@SpringBootApplication(scanBasePackages = {"org.chl"})
public class LogicServerApplication implements CommandLineRunner {

    @Autowired
    private NettyConfig nettyConfig;

    public static void main(String[] args) {
        SpringApplication.run(LogicServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(nettyConfig.getPort());
    }
}
