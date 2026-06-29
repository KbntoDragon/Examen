package com.ventas.ms2.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;

import com.ventas.ms2.DTO.TipoPagoDTO;
import com.ventas.ms2.assemblers.TipoPagoAssembler;
import com.ventas.ms2.model.TipoPago;
import com.ventas.ms2.service.TipoPagoService;

@ExtendWith(MockitoExtension.class)
class TipoPagoControllerTest {

    @Mock
    private TipoPagoService tipoPagoService;

    @Mock
    private TipoPagoAssembler assembler;

    @InjectMocks
    private TipoPagoController tipoPagoController;

    private TipoPagoDTO dto() {
        TipoPagoDTO d = new TipoPagoDTO();
        d.setId(1);
        d.setTipo("Efectivo");
        return d;
    }

    @BeforeEach
    void setUp() {
        lenient().when(assembler.toModel(any(TipoPagoDTO.class)))
                 .thenAnswer(inv -> EntityModel.of((TipoPagoDTO) inv.getArgument(0)));
    }

    @Test
    void listar_conDatos_200() {
        when(tipoPagoService.obtenerTipoPago()).thenReturn(List.of(dto()));
        assertThat(tipoPagoController.listarTipoPago().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void listar_vacio_204() {
        when(tipoPagoService.obtenerTipoPago()).thenReturn(List.of());
        assertThat(tipoPagoController.listarTipoPago().getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void buscarPorId_200() {
        when(tipoPagoService.obtenerTipoPagoDTOPorId(1)).thenReturn(dto());
        assertThat(tipoPagoController.buscarServicioPorId(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void buscarPorId_noExiste_404() {
        when(tipoPagoService.obtenerTipoPagoDTOPorId(9)).thenThrow(new RuntimeException("no"));
        assertThat(tipoPagoController.buscarServicioPorId(9).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void guardar_201() {
        TipoPago tp = new TipoPago();
        tp.setId(1);
        when(tipoPagoService.guardarTipoPago(tp)).thenReturn(tp);
        when(tipoPagoService.obtenerTipoPagoDTOPorId(1)).thenReturn(dto());
        assertThat(tipoPagoController.guardarTipoPago(tp).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void guardar_invalido_400() {
        TipoPago tp = new TipoPago();
        when(tipoPagoService.guardarTipoPago(tp)).thenThrow(new IllegalArgumentException("tipo"));
        assertThat(tipoPagoController.guardarTipoPago(tp).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
