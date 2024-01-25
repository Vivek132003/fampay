package com.fampay.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AssignmentApplication {

	public static final Logger logger = LoggerFactory.getLogger(AssignmentApplication.class);

	public static void main(String[] args) {
		logger.info("Application Started running");
		SpringApplication.run(AssignmentApplication.class, args);

	}

}
