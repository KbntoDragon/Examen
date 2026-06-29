package com.inventario.ms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventarioOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Inventario - Nueva Esperanza")
                .description("API REST para gestionar productos, repuestos y servicios del taller de bicicletas.")
                .version("1.0.0"));
    }
}
