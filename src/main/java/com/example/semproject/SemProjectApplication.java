package com.example.semproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// exclude the spring security here to make sure the static html files are accessible
// Run this file to start the app bound to "localhost:8080/"
// Remember to set the env variable in the spring boot configuration to help it find the big table credential file
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SemProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemProjectApplication.class, args);
    }

}
