package com.ezen.allit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProjectAllitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectAllitApplication.class, args);
	}

}
