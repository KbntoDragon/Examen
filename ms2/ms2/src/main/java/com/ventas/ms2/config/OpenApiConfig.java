package com.ventas.ms2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ventasOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Ventas - Nueva Esperanza")
                .description("API REST para gestionar boletas y tipos de pago. Consume el microservicio de inventario por REST.")
                .version("1.0.0"));
    }
}
