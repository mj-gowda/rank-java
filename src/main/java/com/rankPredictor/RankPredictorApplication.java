package com.rankPredictor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RankPredictorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RankPredictorApplication.class, args);
	}

	// @Bean
	// public WebMvcConfigurer corsConfigurer() {
	// 	return new WebMvcConfigurer() {
	// 		@Override
	// 		public void addCorsMappings(CorsRegistry registry) {
	// 			registry.addMapping("/scrape").allowedOrigins("http://localhost:3000");
	// 			registry.addMapping("/rank").allowedOrigins("http://localhost:3000");
	// 			registry.addMapping("/rankByCategory").allowedOrigins("http://localhost:3000");
	// 		}
	// 	};
	// }

}
