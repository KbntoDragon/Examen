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

import com.inventario.ms.DTO.ServicioDTO;
import com.inventario.ms.assemblers.ServicioAssembler;
import com.inventario.ms.model.Servicio;
import com.inventario.ms.services.ServicioService;

@ExtendWith(MockitoExtension.class)
class ServicioControllerTest {

    @Mock
    private ServicioService servicioService;

    @Mock
    private ServicioAssembler assembler;

    @InjectMocks
    private ServicioController servicioController;

    private ServicioDTO dto() {
        ServicioDTO d = new ServicioDTO();
        d.setId(1);
        d.setNombreServicio("Mantencion");
        return d;
    }

    @BeforeEach
    void setUp() {
        lenient().when(assembler.toModel(any(ServicioDTO.class)))
                 .thenAnswer(inv -> EntityModel.of((ServicioDTO) inv.getArgument(0)));
    }

    @Test
    void listar_conDatos_200() {
        when(servicioService.obtenerServicios()).thenReturn(List.of(dto()));
        assertThat(servicioController.listarServicios().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void listar_vacio_204() {
        when(servicioService.obtenerServicios()).thenReturn(List.of());
        assertThat(servicioController.listarServicios().getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void buscarPorNombre_vacio_204() {
        when(servicioService.buscarPorNombreDTO("x")).thenReturn(List.of());
        assertThat(servicioController.buscarPorNombre("x").getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void buscarPorId_ok_200() {
        when(servicioService.obtenerServicioDTOPorId(1)).thenReturn(dto());
        assertThat(servicioController.buscarProductoPorId(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void buscarPorId_noExiste_404() {
        when(servicioService.obtenerServicioDTOPorId(9)).thenThrow(new RuntimeException("no"));
        assertThat(servicioController.buscarProductoPorId(9).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void guardar_ok_201() {
        Servicio s = new Servicio();
        s.setId(1);
        when(servicioService.guardarServicio(s)).thenReturn(s);
        when(servicioService.obtenerServicioDTOPorId(1)).thenReturn(dto());
        assertThat(servicioController.guardarServicio(s).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void guardar_invalido_400() {
        Servicio s = new Servicio();
        when(servicioService.guardarServicio(s)).thenThrow(new IllegalArgumentException("valor"));
        assertThat(servicioController.guardarServicio(s).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void eliminar_exito_200() {
        when(servicioService.eliminarServicio(1)).thenReturn("Servicio eliminado con exito");
        assertThat(servicioController.eliminarServicio(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void editar_ok_200() {
        Servicio s = new Servicio();
        s.setId(1);
        when(servicioService.guardarServicio(s)).thenReturn(s);
        when(servicioService.obtenerServicioDTOPorId(1)).thenReturn(dto());
        assertThat(servicioController.editarServicio(1, s).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
