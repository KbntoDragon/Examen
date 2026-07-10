package com.persona.ms4.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.persona.ms4.DTO.EmpleadoDTO;
import com.persona.ms4.modelo.Empleado;
import com.persona.ms4.service.EmpleadoService;

@ExtendWith(MockitoExtension.class)
class EmpleadoControllerTest {

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    private EmpleadoDTO dto() {
        EmpleadoDTO d = new EmpleadoDTO();
        d.setId(1);
        d.setNombres("Carla");
        return d;
    }

    @Test
    void todos_200() {
        when(empleadoService.obtenerTodos()).thenReturn(List.of(dto()));
        assertThat(empleadoController.obtenerTodos().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void porId_ok_200() {
        when(empleadoService.buscarPorId(1)).thenReturn(dto());
        assertThat(empleadoController.buscarPorId(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void porId_noExiste_404() {
        when(empleadoService.buscarPorId(9)).thenThrow(new RuntimeException("no"));
        assertThat(empleadoController.buscarPorId(9).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void buscarPorNombres_conDatos_200() {
        Empleado e = new Empleado();
        e.setNombres("Carla");
        when(empleadoService.buscarPorNombres("Carla")).thenReturn(List.of(dto()));
        assertThat(empleadoController.buscarPorNombres("Carla").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void registrar_ok_201() {
        Empleado e = new Empleado();
        when(empleadoService.guardarEmpleado(e)).thenReturn(dto());
        assertThat(empleadoController.agregarEmpleado(e).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void registrar_error_400() {
        Empleado e = new Empleado();
        when(empleadoService.guardarEmpleado(e)).thenThrow(new RuntimeException("error"));
        assertThat(empleadoController.agregarEmpleado(e).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void actualizar_ok_200() {
        Empleado e = new Empleado();
        when(empleadoService.actualizarEmpleado(1, e)).thenReturn(dto());
        assertThat(empleadoController.actualizarEmpleado(1, e).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void eliminar_exito_200() {
        when(empleadoService.eliminar(1)).thenReturn("Empleado eliminado exitosamente");
        assertThat(empleadoController.eliminarEmpleado(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}