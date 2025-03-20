package org.wangqing.microservices.edge;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableZuulProxy
@RestController
public class Application {

    // 设置明文用户名和密码
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    @Bean
    public AlwaysSampler defaultSampler() {
        return new AlwaysSampler();
    }

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            return "Login successful!";
        } else {
            return "Invalid username or password!";
        }
    }

    @GetMapping("/secure-data")
    public String getSecureData(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.equals("Basic " + encodeBase64(USERNAME + ":" + PASSWORD))) {
            return "Unauthorized! Provide correct credentials.";
        }
        return "Here is your secure data.";
    }

    private String encodeBase64(String value) {
        return java.util.Base64.getEncoder().encodeToString(value.getBytes());
    }
}
