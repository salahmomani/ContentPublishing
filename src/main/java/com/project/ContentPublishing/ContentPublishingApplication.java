package com.project.ContentPublishing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ContentPublishingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentPublishingApplication.class, args);
	}

}
