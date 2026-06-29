package com.bicicleta.ms3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bicicletaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Microservicio de Bicicletas - Nueva Esperanza")
                .description("API REST para gestionar bicicletas, marcas, modelos y colores.")
                .version("1.0.0"));
    }
}
