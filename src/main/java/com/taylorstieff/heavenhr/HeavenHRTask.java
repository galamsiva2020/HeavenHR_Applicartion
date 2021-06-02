package com.taylorstieff.heavenhr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.taylorstieff.heavenhr")
public class HeavenHRTask {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(HeavenHRTask.class, args);
    }
}
