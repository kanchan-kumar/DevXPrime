package org.devxprime.main;

import org.devxprime.web.props.StorageProperties;
import org.devxprime.web.services.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages="org.devxprime.web.controllers, org.devxprime.web.exception, "
		+ "org.devxprime.web.services, org.devxprime.web.props, org.devxprime.utils, org.devxprime.tools.decompilers")
@EnableConfigurationProperties(StorageProperties.class)
public class DevToolKitXPrimeApplication extends SpringBootServletInitializer {

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
}

