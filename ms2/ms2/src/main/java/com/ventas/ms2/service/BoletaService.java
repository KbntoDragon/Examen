package com.ventas.ms2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.ventas.ms2.DTO.BoletaDTO;
import com.ventas.ms2.DTO.ProductoDTO;
import com.ventas.ms2.model.Boleta;
import com.ventas.ms2.repository.BoletaRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Transactional
@Slf4j
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${ms.inventario.url:http://localhost:8081}")
    private String inventarioUrl;

    public BoletaDTO convertirADTO(Boleta boleta) {
        BoletaDTO dto = new BoletaDTO();
        dto.setId(boleta.getId());
        dto.setPrecio(boleta.getTotal());
        dto.setTipoPago(boleta.getTipoPago() != null ? boleta.getTipoPago().getTipo() : null);
        dto.setEmpleadoId(boleta.getEmpleadoIds());
        dto.setProductoId(boleta.getProductoIds());
        dto.setRepuestoId(boleta.getRepuestoIds());
        dto.setServicioId(boleta.getServicioIds());
        return dto;
    }

    public List<BoletaDTO> obtenerBoletas() {
        log.info("Obteniendo todas las boletas");
        return boletaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public BoletaDTO buscarPorId(Integer id) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada con id: " + id));
        return convertirADTO(boleta);
    }

    public Boleta guardarBoleta(Boleta boleta) {
        if (boleta.getTotal() == null || boleta.getTotal() < 0) {
            throw new IllegalArgumentException("El total de la boleta no puede ser negativo");
        }
        return boletaRepository.save(boleta);
    }

    public String eliminar(Integer id) {
        if (!boletaRepository.existsById(id)) {
            return "Boleta no encontrada con id: " + id;
        }
        boletaRepository.deleteById(id);
        return "Boleta eliminada con exito";
    }

    public Boleta agregarProducto(Integer boletaId, Integer productoId) {
        Boleta boleta = boletaRepository.findById(boletaId)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada con id: " + boletaId));

        ProductoDTO producto = webClientBuilder.build().get()
                .uri(inventarioUrl + "/api/v1/productos/{id}", productoId)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        resp -> Mono.error(new RuntimeException(
                                "El producto " + productoId + " no existe en el inventario")))
                .bodyToMono(ProductoDTO.class)
                .block();

        if (producto == null) {
            throw new RuntimeException("No se pudo obtener el producto " + productoId + " del inventario");
        }

        if (boleta.getProductoIds() == null) {
            boleta.setProductoIds(new ArrayList<>());
        }
        boleta.getProductoIds().add(productoId);
        return boletaRepository.save(boleta);
    }
}
