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

import com.ventas.ms2.DTO.BoletaDTO;
import com.ventas.ms2.assemblers.BoletaAssembler;
import com.ventas.ms2.model.Boleta;
import com.ventas.ms2.service.BoletaService;

@ExtendWith(MockitoExtension.class)
class BoletaControllerTest {

    @Mock
    private BoletaService boletaService;

    @Mock
    private BoletaAssembler assembler;

    @InjectMocks
    private BoletaController boletaController;

    private BoletaDTO dto() {
        BoletaDTO d = new BoletaDTO();
        d.setId(1);
        d.setPrecio(1000.0);
        return d;
    }

    @BeforeEach
    void setUp() {
        lenient().when(assembler.toModel(any(BoletaDTO.class)))
                 .thenAnswer(inv -> EntityModel.of((BoletaDTO) inv.getArgument(0)));
    }

    @Test
    void obtenerBoletas_conDatos_200() {
        when(boletaService.obtenerBoletas()).thenReturn(List.of(dto()));
        assertThat(boletaController.obtenerBoletas().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void obtenerBoletas_vacio_204() {
        when(boletaService.obtenerBoletas()).thenReturn(List.of());
        assertThat(boletaController.obtenerBoletas().getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void buscarPorId_200() {
        when(boletaService.buscarPorId(1)).thenReturn(dto());
        assertThat(boletaController.buscarPorId(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void guardarBoleta_201() {
        Boleta b = new Boleta();
        when(boletaService.guardarBoleta(b)).thenReturn(b);
        when(boletaService.convertirADTO(b)).thenReturn(dto());
        assertThat(boletaController.guardarBoleta(b).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void eliminarBoleta_200() {
        when(boletaService.eliminar(1)).thenReturn("Boleta eliminada con exito");
        assertThat(boletaController.eliminarBoleta(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void agregarProducto_200() {
        Boleta b = new Boleta();
        b.setId(1);
        when(boletaService.agregarProducto(1, 2)).thenReturn(b);
        when(boletaService.convertirADTO(b)).thenReturn(dto());
        assertThat(boletaController.agregarProducto(1, 2).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
