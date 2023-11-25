package com.sample.crm.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig. 2020/11/20 3:45 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI createRestApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Relationship Management")
                        .description("CRM project(API only)")
                        .version("1.0.0"));
    }

}
