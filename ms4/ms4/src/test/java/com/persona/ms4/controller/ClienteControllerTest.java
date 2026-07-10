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

import com.persona.ms4.DTO.ClienteDTO;
import com.persona.ms4.modelo.Cliente;
import com.persona.ms4.service.ClienteService;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private ClienteDTO dto() {
        ClienteDTO d = new ClienteDTO();
        d.setId(1);
        d.setNombres("Ana");
        return d;
    }

    @Test
    void todos_200() {
        when(clienteService.obtenerTodos()).thenReturn(List.of(dto()));
        assertThat(clienteController.obtenerTodos().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void porId_ok_200() {
        when(clienteService.buscarPorId(1)).thenReturn(dto());
        assertThat(clienteController.buscarPorId(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void porId_noExiste_404() {
        when(clienteService.buscarPorId(9)).thenThrow(new RuntimeException("no"));
        assertThat(clienteController.buscarPorId(9).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void registrar_ok_201() {
        Cliente c = new Cliente();
        when(clienteService.guardarCliente(c)).thenReturn(dto());
        assertThat(clienteController.agregarCliente(c).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void registrar_error_400() {
        Cliente c = new Cliente();
        when(clienteService.guardarCliente(c)).thenThrow(new RuntimeException("error"));
        assertThat(clienteController.agregarCliente(c).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void eliminar_200() {
        when(clienteService.eliminar(1)).thenReturn("Cliente eliminado con exito");
        assertThat(clienteController.eliminarCliente(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void buscarPorCorreo_200() {
        when(clienteService.buscarPorCorreos("a@mail.cl")).thenReturn(List.of(dto()));
        assertThat(clienteController.buscraPorCorreos("a@mail.cl").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void buscarPorNombre_conDatos_200() {
        Cliente c = new Cliente();
        c.setNombres("Ana");
        when(clienteService.buscarPorNombres("Ana")).thenReturn(List.of(dto()));
        assertThat(clienteController.buscarPorNombres("Ana").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}