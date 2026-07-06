package com.rajat.user_api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI userManagementOpenAPI() {
	    return new OpenAPI()
	            .info(new Info()
	                    .title("User Management API")
	                    .version("1.0.0")
	                    .description("Production-ready Spring Boot REST API built for enterprise SDET practice.")
	                    .contact(new Contact()
	                            .name("Rajat Lahariya")
	                            .email("rajat2496@gmail.com"))
	                    .license(new License()
	                            .name("MIT License")))
	            .tags(List.of(
	                    new Tag().name("User Management")
	                            .description("APIs for user CRUD, filtering, pagination, sorting and soft delete"),
	                    new Tag().name("Authentication")
	                            .description("APIs for login, token generation and authentication"),
	                    new Tag().name("Health Check")
	                            .description("APIs for checking application health")
	            ))
	            .servers(List.of(
	            	    new Server()
	            	            .url("http://localhost:8081")
	            	            .description("Local Development Server")
	            	))
	            .externalDocs(
	            	    new ExternalDocumentation()
	            	            .description("GitHub Repository")
	            	            .url("https://github.com/rajat2496/user-api")
	            	)
	            ;
	}
}