package com.gmail.phrolovich;

import com.gmail.phrolovich.integration.PushshiftHttpEventStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class UBNTHomeworkApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UBNTHomeworkApplication.class, args);
    }

}
