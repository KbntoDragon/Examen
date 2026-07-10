package com.persona.ms4.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.persona.ms4.DTO.BicicletaExternoDTO;
import com.persona.ms4.modelo.Cliente;

import reactor.core.publisher.Mono;

@Service
public class ClienteValidaciones {

    private final WebClient.Builder webClientBuilder;

    ClienteValidaciones(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Boolean validarNullVacio(Cliente cliente) {
        if(cliente.getNombres() == null || cliente.getNombres().trim().length() == 0) {
            return false;
        }
        if(cliente.getApellidos() == null || cliente.getApellidos().trim().length() == 0) {
            return false;
        }
        return true;
    }

    public BicicletaExternoDTO obtenerBicicleta(Integer id) {
        BicicletaExternoDTO bicicleta = new BicicletaExternoDTO();
        try {
            bicicleta = webClientBuilder.build()
                .get()
                .uri("http://localhost:8084/api/v1/bicicletas/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(BicicletaExternoDTO.class)
                .block();

            return bicicleta;

        } catch (Exception e) {
            bicicleta.setId(0);
            bicicleta.setMaterial("No se encontró la bicicleta");
            bicicleta.setClienteNombre("No se encontró la bicicleta");
            bicicleta.setModeloNombre("No se encontró la bicicleta");
            bicicleta.setColores("No se encontró la bicicleta");
            bicicleta.setMarcas("No se encontró la bicicleta");
            return bicicleta;
        }
    }

}