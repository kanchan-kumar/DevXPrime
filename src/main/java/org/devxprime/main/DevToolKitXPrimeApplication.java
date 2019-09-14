package org.devxprime.main;

import org.devxprime.web.props.StorageProperties;
import org.devxprime.web.services.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages="org.devxprime.web.controllers, org.devxprime.web.exception, "
		+ "org.devxprime.web.services, org.devxprime.web.props, org.devxprime.utils")
@EnableConfigurationProperties(StorageProperties.class)
public class DevToolKitXPrimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevToolKitXPrimeApplication.class, args);
	}
	
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            // storageService.deleteAll();
            storageService.init();
        };
    }
    
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //registry.addMapping("/**").allowedOrigins("https://www.devprimetools.com");
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}

