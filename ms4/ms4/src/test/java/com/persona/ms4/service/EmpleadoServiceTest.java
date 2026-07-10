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

import com.persona.ms4.DTO.EmpleadoDTO;
import com.persona.ms4.modelo.Empleado;
import com.persona.ms4.repository.EmpleadoRepository;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    private Empleado nuevoEmpleado(Integer id, String nombres) {
        Empleado e = new Empleado();
        e.setId(id);
        e.setNombres(nombres);
        e.setApellidos("Soto");
        return e;
    }

    @Test
    void findAll_devuelveDTOs() {
        when(empleadoRepository.findAll()).thenReturn(List.of(nuevoEmpleado(1, "Carla")));
        List<EmpleadoDTO> resultado = empleadoService.obtenerTodos();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombres()).isEqualTo("Carla");
    }

    @Test
    void buscarPorId_noExiste_lanza() {
        when(empleadoRepository.findById(3)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> empleadoService.buscarPorId(3))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void guardarEmpleado_devuelveDTO() {
        Empleado e = nuevoEmpleado(null, "Pedro");
        when(empleadoRepository.save(e)).thenReturn(nuevoEmpleado(1, "Pedro"));
        assertThat(empleadoService.guardarEmpleado(e).getId()).isEqualTo(1);
    }

    @Test
    void buscarPorNombre_sinResultados_lanza() {
        when(empleadoRepository.findByNombresContainingIgnoreCase("Zoe")).thenReturn(List.of());
        assertThatThrownBy(() -> empleadoService.buscarPorNombres("Zoe"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void actualizarEmpleado_existente_actualiza() {
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(nuevoEmpleado(1, "Viejo")));
        when(empleadoRepository.save(org.mockito.ArgumentMatchers.any(Empleado.class)))
                .thenAnswer(inv -> inv.getArgument(0));
        EmpleadoDTO actualizado = empleadoService.actualizarEmpleado(1, nuevoEmpleado(null, "Nuevo"));
        assertThat(actualizado.getNombres()).isEqualTo("Nuevo");
    }

    @Test
    void eliminarPorId_existe_borra() {
        when(empleadoRepository.findById(1)).thenReturn(Optional.of(nuevoEmpleado(1, "Carla")));
        assertThat(empleadoService.eliminar(1)).contains("exito");
        verify(empleadoRepository).delete(org.mockito.ArgumentMatchers.any(Empleado.class));
    }
}