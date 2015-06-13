package com.maxmatveev.soccer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by Max Matveev on 09/06/15.
 */
@SpringBootApplication
public class Bootstrap {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Bootstrap.class, args);
    }
}
