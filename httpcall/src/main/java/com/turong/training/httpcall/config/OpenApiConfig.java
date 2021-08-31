package com.turong.training.httpcall.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI exampleOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Example API Specification")
                        .description("SpringBoot Example application")
                        .version("v1.0")
                        .license(new License().name("copyright@turong").url("http://turong.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("(^turong software consulting group^)")
                        .url("https://turong.com/software"));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi pingApi() {
        return GroupedOpenApi.builder()
                .group("ping")
                .pathsToMatch("/ping/**")
                .build();
    }

}
