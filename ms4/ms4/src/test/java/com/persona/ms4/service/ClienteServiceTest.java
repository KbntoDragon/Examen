package com.persona.ms4.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.persona.ms4.DTO.ClienteDTO;
import com.persona.ms4.modelo.Cliente;
import com.persona.ms4.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente nuevoCliente(Integer id, String nombres, String correo) {
        Cliente c = new Cliente();
        c.setId(id);
        c.setNombres(nombres);
        c.setApellidos("Perez");
        c.setRut("11111111-1");
        c.setCorreo(correo);
        c.setTelefono("912345678");
        return c;
    }

    @Test
    void findAll_devuelveDTOs() {
        when(clienteRepository.findAll()).thenReturn(List.of(nuevoCliente(1, "Ana", "ana@mail.cl")));
        List<ClienteDTO> resultado = clienteService.obtenerTodos();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombres()).isEqualTo("Ana");
    }

    @Test
    void buscarPorId_noExiste_lanza() {
        when(clienteRepository.findById(5)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> clienteService.buscarPorId(5))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void guardarCliente_devuelveDTO() {
        Cliente c = nuevoCliente(null, "Luis", "luis@mail.cl");
        when(clienteRepository.save(c)).thenReturn(nuevoCliente(1, "Luis", "luis@mail.cl"));
        ClienteDTO dto = clienteService.guardarCliente(c);
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getCorreo()).isEqualTo("luis@mail.cl");
    }

    @Test
    void buscarPorCorreo_noExiste_lanza() {
        when(clienteRepository.findByCorreo("x@mail.cl")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> clienteService.buscarPorCorreos("x@mail.cl"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void buscarPorNombre_sinResultados_lanza() {
        when(clienteRepository.findByNombres("Zoe")).thenReturn(List.of());
        assertThatThrownBy(() -> clienteService.buscarPorNombres("Zoe"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void eliminarPorId_existe_borra() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(nuevoCliente(1, "Ana", "ana@mail.cl")));
        assertThat(clienteService.eliminar(1)).contains("exito");
        verify(clienteRepository).delete(org.mockito.ArgumentMatchers.any(Cliente.class));
    }
}