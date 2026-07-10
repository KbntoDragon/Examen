package com.persona.ms4.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.persona.ms4.DTO.BoletaExternoDTO;
import com.persona.ms4.modelo.Empleado;

import reactor.core.publisher.Mono;

@Service
public class EmpleadoValidaciones {

    private final WebClient.Builder webClientBuilder;

    EmpleadoValidaciones(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Boolean validarNullVacio(Empleado empleado) {
        if(empleado.getNombres() == null || empleado.getNombres().trim().length() == 0){
            return false;
        }
        if(empleado.getApellidos() == null || empleado.getApellidos().trim().length() == 0){
            return false;
        }
        return true;
    }

    public BoletaExternoDTO obtenerBoleta(Integer id) {
        BoletaExternoDTO empleado = new BoletaExternoDTO();
        try {
            empleado = webClientBuilder.build()
                .get()
                .uri("http://localhost:8084/api/v1/empleados/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty()) // Importante
                .bodyToMono(BoletaExternoDTO.class)
                .block();

            return empleado;

        } catch (Exception e) {
            empleado.setId(0);
            empleado.setPrecio(0.0);
            empleado.setTipoPago(null);
            return empleado;
        }
    }

}