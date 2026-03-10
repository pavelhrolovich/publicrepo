package com.gmail.phrolovich.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ec2DashboardOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("EC2 Dashboard API")
                .description("Example of EC2 Dashboard API")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Pavel Hrolovich")
                    .email("pavelhrolovic@gmail.com")))
            .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
            .schemaRequirement("basicAuth", new SecurityScheme()
                .name("basicAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic"));
    }
}
