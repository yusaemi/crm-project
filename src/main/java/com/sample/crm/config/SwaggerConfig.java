package com.sample.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * SwaggerConfig. 2020/11/20 3:45 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true).select()
                .apis(RequestHandlerSelectors.basePackage("com.sample.crm"))
                .paths(PathSelectors.any()).build().pathMapping("/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Customer Relationship Management")
                .description("CRM project(API only)")
                .version("1.0.0")
                .build();
    }

}
