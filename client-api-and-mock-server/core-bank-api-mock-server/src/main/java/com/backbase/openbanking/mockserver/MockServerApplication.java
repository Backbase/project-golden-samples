package com.backbase.openbanking.mockserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MockServerApplication {

    public static void main(String[] args) throws Exception {
        new SpringApplication(MockServerApplication.class).run(args);
    }
}
