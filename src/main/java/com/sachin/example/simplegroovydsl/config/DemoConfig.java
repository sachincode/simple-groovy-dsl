package com.sachin.example.simplegroovydsl.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableScheduling
@EnableSwagger2
@Configuration
@MapperScan("com.sachin.example.simplegroovydsl.dao")
public class DemoConfig {


    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("simple-groovy-dsl").select()
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.sachin.example.simplegroovydsl.controller"))
                .paths(PathSelectors.any())
//                .paths(regex("/(" + serviceInfo.getServiceName() + "/).*"))
                .build()
                .apiInfo(new ApiInfoBuilder().title("simple-groovy-dsl").description("simple-groovy-dsl 相关Api").version("1.0").build())
                .useDefaultResponseMessages(false);
    }


}
