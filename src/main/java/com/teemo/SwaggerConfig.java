package com.teemo;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by tangjingxiang on 2016/11/2.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.teemo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring-Boot-Demo-API")
                .description("©2016 Copyright. Powered By Teemo.")
                .termsOfServiceUrl("")
                .contact(new Contact("Teemo", "", "Teemo@163.com"))
                .version("2.0")
                .build();
    }

}
