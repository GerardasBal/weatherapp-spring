package com.gerardas.weatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import static spark.Spark.*;

@SpringBootApplication
@EnableScheduling
public class WeatherappApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WeatherappApplication.class);
    }


    public static void main(String[] args) {
        staticFiles.location("/static");
        SpringApplication.run(WeatherappApplication.class, args);
    }


}
