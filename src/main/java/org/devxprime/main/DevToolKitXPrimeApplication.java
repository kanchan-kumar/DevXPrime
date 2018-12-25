package org.devxprime.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="org.devxprime.web.controllers")
public class DevToolKitXPrimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevToolKitXPrimeApplication.class, args);
	}

}

