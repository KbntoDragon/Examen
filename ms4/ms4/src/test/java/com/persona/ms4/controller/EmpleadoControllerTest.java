package com.persona.ms4.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;

import com.persona.ms4.DTO.EmpleadoDTO;
import com.persona.ms4.assemblers.EmpleadoModelAssembler;
import com.persona.ms4.modelo.Empleado;
import com.persona.ms4.service.EmpleadoService;

@ExtendWith(MockitoExtension.class)
class EmpleadoControllerTest {

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private EmpleadoModelAssembler assembler;

    @InjectMocks
    private EmpleadoController empleadoController;

    private EmpleadoDTO dto() {
        EmpleadoDTO d = new EmpleadoDTO();
        d.setId(1);
        d.setNombres("Carla");
        return d;
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        lenient().when(assembler.toModel(any(EmpleadoDTO.class)))
                 .thenAnswer(inv -> EntityModel.of((EmpleadoDTO) inv.getArgument(0)));
    }

    @Test
    void todos_200() {
        when(empleadoService.findAll()).thenReturn(List.of(dto()));
        assertThat(empleadoController.todos().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void porId_ok_200() {
        when(empleadoService.buscarPorId(1)).thenReturn(dto());
        assertThat(empleadoController.porId(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void porId_noExiste_404() {
        when(empleadoService.buscarPorId(9)).thenThrow(new RuntimeException("no"));
        assertThat(empleadoController.porId(9).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void buscarPorNombres_conDatos_200() {
        Empleado e = new Empleado();
        e.setNombres("Carla");
        when(empleadoService.buscarPorNombre("Carla")).thenReturn(List.of(e));
        assertThat(empleadoController.buscarPorNombres("Carla").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void registrar_ok_201() {
        Empleado e = new Empleado();
        when(empleadoService.guardarEmpleado(e)).thenReturn(dto());
        assertThat(empleadoController.registrar(e).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void registrar_error_400() {
        Empleado e = new Empleado();
        when(empleadoService.guardarEmpleado(e)).thenThrow(new RuntimeException("error"));
        assertThat(empleadoController.registrar(e).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void actualizar_ok_200() {
        Empleado e = new Empleado();
        when(empleadoService.actualizarEmpleado(1, e)).thenReturn(e);
        assertThat(empleadoController.actualizarEmpleado(1, e).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void eliminar_exito_200() {
        when(empleadoService.eliminarPorId(1)).thenReturn("Empleado eliminado exitosamente");
        assertThat(empleadoController.eliminarEmpleado(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
