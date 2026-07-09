package com.ventas.ms2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ventas.ms2.DTO.TipoPagoDTO;
import com.ventas.ms2.model.Boleta;
import com.ventas.ms2.model.TipoPago;
import com.ventas.ms2.repository.TipoPagoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class TipoPagoService {

    @Autowired
    private TipoPagoRepository tipoPagoRepository;

    private TipoPagoDTO convertirADTO(TipoPago tipoPago) {
        TipoPagoDTO dto = new TipoPagoDTO();
        dto.setId(tipoPago.getId());
        dto.setTipo(tipoPago.getTipo());
        if (tipoPago.getBoletas() != null) {
            dto.setBoletaId(tipoPago.getBoletas().stream().map(Boleta::getId).toList());
        }
        return dto;
    }

    public List<TipoPagoDTO> obtenerTipoPago() {
        log.info("Obteniendo todos los tipos de pago");
        return tipoPagoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public TipoPagoDTO obtenerTipoPagoDTOPorId(Integer id) {
        TipoPago tipoPago = tipoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de pago no encontrado con id: " + id));
        return convertirADTO(tipoPago);
    }

    public TipoPago guardarTipoPago(TipoPago tipoPago) {
        if (tipoPago.getTipo() == null || tipoPago.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de pago es obligatorio");
        }
        return tipoPagoRepository.save(tipoPago);
    }
}
