package com.nw.intern.bu3internecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@SpringBootApplication
public class Bu3InternEcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Bu3InternEcommerceApplication.class, args);
    }

}
