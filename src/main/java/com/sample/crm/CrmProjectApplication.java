package com.sample.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.token.Sha512DigestUtils;

import java.math.BigInteger;
import java.util.UUID;

@SpringBootApplication
public class CrmProjectApplication {

    void main() {
        SpringApplication.run(CrmProjectApplication.class);
    }

    @Bean
    public String jwtSignKey() {
        byte[] b = Sha512DigestUtils.sha(UUID.randomUUID().toString());
        return new BigInteger(1, b).toString(16).toUpperCase();
    }

}
