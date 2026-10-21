package org.wangqing.microservices.edge;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
		String username = "admin";
        	String password = "securePassword123";

        	System.out.println("Username: " + username);
        	System.out.println("Password: " + password);
	}
	
} 
