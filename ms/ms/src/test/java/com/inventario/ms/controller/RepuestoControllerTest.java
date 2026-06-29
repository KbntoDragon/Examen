package com.inventario.ms.controller;

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

import com.inventario.ms.DTO.RepuestoDTO;
import com.inventario.ms.assemblers.RepuestoAssembler;
import com.inventario.ms.model.Repuesto;
import com.inventario.ms.services.RepuestoService;

@ExtendWith(MockitoExtension.class)
class RepuestoControllerTest {

    @Mock
    private RepuestoService repuestoService;

    @Mock
    private RepuestoAssembler assembler;

    @InjectMocks
    private RepuestoController repuestoController;

    private RepuestoDTO dto() {
        RepuestoDTO d = new RepuestoDTO();
        d.setId(1);
        d.setNombreRepuesto("Freno");
        return d;
    }

    @BeforeEach
    void setUp() {
        lenient().when(assembler.toModel(any(RepuestoDTO.class)))
                 .thenAnswer(inv -> EntityModel.of((RepuestoDTO) inv.getArgument(0)));
    }

    @Test
    void listar_conDatos_devuelve200() {
        when(repuestoService.obtenerRepuestos()).thenReturn(List.of(dto()));
        assertThat(repuestoController.listarProductos().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void listar_vacio_devuelve204() {
        when(repuestoService.obtenerRepuestos()).thenReturn(List.of());
        assertThat(repuestoController.listarProductos().getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void buscarPorId_ok() {
        when(repuestoService.obtenerRepuestoDTOPorId(1)).thenReturn(dto());
        assertThat(repuestoController.buscarProductoPorId(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void buscarPorId_noExiste_404() {
        when(repuestoService.obtenerRepuestoDTOPorId(9)).thenThrow(new RuntimeException("no"));
        assertThat(repuestoController.buscarProductoPorId(9).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void guardar_ok_201() {
        Repuesto r = new Repuesto();
        r.setId(1);
        when(repuestoService.guardarRepuesto(r)).thenReturn(r);
        when(repuestoService.obtenerRepuestoDTOPorId(1)).thenReturn(dto());
        assertThat(repuestoController.guardarRepuesto(r).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void guardar_invalido_400() {
        Repuesto r = new Repuesto();
        when(repuestoService.guardarRepuesto(r)).thenThrow(new IllegalArgumentException("precio"));
        assertThat(repuestoController.guardarRepuesto(r).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void eliminar_exito_200() {
        when(repuestoService.eliminarRepuesto(1)).thenReturn("Repuesto eliminado con exito");
        assertThat(repuestoController.eliminarProducto(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void eliminar_noExiste_404() {
        when(repuestoService.eliminarRepuesto(9)).thenReturn("Repuesto no encontrado con id: 9");
        assertThat(repuestoController.eliminarProducto(9).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void buscarPorNombre_vacio_204() {
        when(repuestoService.buscarPorNombreDTO("x")).thenReturn(List.of());
        assertThat(repuestoController.buscarPorNombre("x").getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void buscarPorCodigo_ok_200() {
        when(repuestoService.buscarPorCodigoBarraDTO("R-1")).thenReturn(List.of(dto()));
        assertThat(repuestoController.buscarPorCodigoBarra("R-1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void sinStock_ok_200() {
        when(repuestoService.obtenerRepuestosSinStockDTO()).thenReturn(List.of(dto()));
        assertThat(repuestoController.sinStock().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void editar_ok_200() {
        Repuesto r = new Repuesto();
        r.setId(1);
        when(repuestoService.guardarRepuesto(r)).thenReturn(r);
        when(repuestoService.obtenerRepuestoDTOPorId(1)).thenReturn(dto());
        assertThat(repuestoController.editarRepuestos(1, r).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
