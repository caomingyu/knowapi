package com.cmy.knowapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.cmy.knowapi.mapper")
@EnableCaching
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@Slf4j
public class KnowapiApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(KnowapiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("----------------启动springboot项目--------------");
    }
}
