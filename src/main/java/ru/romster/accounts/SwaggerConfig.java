package ru.romster.accounts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Created by n.romanov
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.romster.accounts.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }

    @Bean
    public UiConfiguration uiConfig() {
        return new UiConfiguration(null, "list",
                "alpha", "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false,
                true, null);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Тестовое задание для компании ООО «Центр недвижимости от Сбербанка»",
                "Веб-приложение, цель которого - операции со счетами пользователей",
                "1.0",
                null,
                new Contact("Николай Романов", null, "romster90@gmail.com"),
                null,
                null,
                Collections.emptyList());
    }


}