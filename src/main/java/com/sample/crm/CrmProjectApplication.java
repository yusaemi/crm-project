package com.sample.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.token.Sha512DigestUtils;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.math.BigInteger;
import java.util.UUID;

@EnableOpenApi
@SpringBootApplication
public class CrmProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmProjectApplication.class, args);
	}

	@Bean
	public String jwtSignKey() {
		byte[] b = Sha512DigestUtils.sha(UUID.randomUUID().toString());
		return new BigInteger(1, b).toString(16).toUpperCase();
	}

}
