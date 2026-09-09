package com.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrainingManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingManagementApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  TMS Spring Boot Backend Running on Port 8080   ");
        System.out.println("  H2 Database Console: http://localhost:8080/h2-console ");
        System.out.println("=================================================");
    }
}
