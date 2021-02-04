package com.egs.batching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.egs.*")
@EnableAspectJAutoProxy
public class MySQLBatchingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySQLBatchingApplication.class, args);
    }
}
