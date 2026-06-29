package com.bicicleta.ms3.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bicicleta.ms3.DTO.ClienteExternoDTO;
import com.bicicleta.ms3.model.Bicicleta;

import reactor.core.publisher.Mono;

@Service
public class BicicletaValidaciones {

    private final WebClient.Builder webClientBuilder;

    BicicletaValidaciones(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Boolean validarNull(Bicicleta bicicleta) {
        if (bicicleta == null) return false;
       if(bicicleta.getMaterial() == null || bicicleta.getMaterial().trim().length() == 0){
            return false;
        }
        return true;
    }

    public ClienteExternoDTO validarCliente(Integer id) {       
        ClienteExternoDTO cliente = new ClienteExternoDTO();
        try {
             cliente = webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/api/v1/clientes/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty()) 
                .bodyToMono(ClienteExternoDTO.class)
                .block();
                    
           return cliente;
    
        } catch (Exception e) {
            cliente.setClienteId(0);
            cliente.setBicicletaId(id);
            cliente.setNombreCliente("Cliente no encontrado.");
            return cliente;
        }
    }
}    
