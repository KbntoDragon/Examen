package com.inventario.ms.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inventario.ms.DTO.ServicioDTO;
import com.inventario.ms.model.Servicio;
import com.inventario.ms.repository.ServicioRepository;

@ExtendWith(MockitoExtension.class)
class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioService servicioService;

    private Servicio nuevoServicio(Integer id, String nombre, double valor) {
        Servicio s = new Servicio();
        s.setId(id);
        s.setNombreServicio(nombre);
        s.setDescServicio("desc");
        s.setValorDelServicio(valor);
        return s;
    }

    @Test
    void obtenerServicios_devuelveDTOs() {
        when(servicioRepository.findAll()).thenReturn(List.of(nuevoServicio(1, "Mantencion", 15000.0)));
        List<ServicioDTO> resultado = servicioService.obtenerServicios();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreServicio()).isEqualTo("Mantencion");
    }

    @Test
    void obtenerServicioDTOPorId_noExiste_lanza() {
        when(servicioRepository.findById(7)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> servicioService.obtenerServicioDTOPorId(7))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void guardarServicio_valorInvalido_lanza() {
        Servicio s = nuevoServicio(null, "Mantencion", 0.0);
        assertThatThrownBy(() -> servicioService.guardarServicio(s))
                .isInstanceOf(IllegalArgumentException.class);
        verify(servicioRepository, never()).save(any());
    }

    @Test
    void guardarServicio_valido_persiste() {
        Servicio s = nuevoServicio(null, "Mantencion", 15000.0);
        when(servicioRepository.save(s)).thenReturn(nuevoServicio(1, "Mantencion", 15000.0));
        assertThat(servicioService.guardarServicio(s).getId()).isEqualTo(1);
    }

    @Test
    void eliminarServicio_noExiste_noBorra() {
        when(servicioRepository.existsById(9)).thenReturn(false);
        assertThat(servicioService.eliminarServicio(9)).contains("no encontrado");
        verify(servicioRepository, never()).deleteById(any());
    }
}
