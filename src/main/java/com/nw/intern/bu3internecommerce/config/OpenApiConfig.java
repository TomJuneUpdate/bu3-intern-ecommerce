package com.nw.intern.bu3internecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger config
 */
@Configuration
public class OpenApiConfig {
    final String securitySchemeName = "BearerAuth";
    @Bean
    public GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs) {
        return GroupedOpenApi.builder().group(apiDocs) // /v3/api-docs/api service
                .packagesToScan("com.nw.intern.bu3internecommerce.controller") //.pathsToMatch(/**)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${openapi.service.title}") String title,
                                 @Value("${openapi.service.version}") String version ){
        License license = new License();
        license.setUrl("https://springdoc.org");
        license.setName("Apache 2");
        Info info = new Info()
                .title(title)
                .version(version)
                .description("Backend API demo").license(license);

        Server devServer = new Server();
        devServer.setUrl("http://localhost:1503");
        devServer.setDescription("Development environment");

        Server prodServer = new Server();
        prodServer.setUrl("https://abc");
        prodServer.setDescription("Production environment");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer,prodServer))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)) // Thêm bảo mật vào API
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")  // Sử dụng JWT token
                        )
                );
    }
}
