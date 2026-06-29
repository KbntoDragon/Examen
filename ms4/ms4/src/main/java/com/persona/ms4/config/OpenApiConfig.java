package com.persona.ms4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI personaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Personas - Nueva Esperanza")
                .description("API REST para gestionar clientes y empleados.")
                .version("1.0.0"));
    }
}
