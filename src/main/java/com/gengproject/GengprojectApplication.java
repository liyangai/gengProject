package com.gengproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.gengproject.dao")
@SpringBootApplication
public class GengprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(GengprojectApplication.class, args);
    }

}
