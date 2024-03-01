package com.example.triplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TriplannerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TriplannerApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TriplannerApplication.class);
	}

}
