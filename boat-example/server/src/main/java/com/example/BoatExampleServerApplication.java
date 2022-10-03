package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BoatExampleServerApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(BoatExampleServerApplication.class, args);
    }

}