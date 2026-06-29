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

import com.inventario.ms.DTO.RepuestoDTO;
import com.inventario.ms.model.Repuesto;
import com.inventario.ms.repository.RepuestoRepository;

@ExtendWith(MockitoExtension.class)
class RepuestoServiceTest {

    @Mock
    private RepuestoRepository repuestoRepository;

    @InjectMocks
    private RepuestoService repuestoService;

    private Repuesto nuevoRepuesto(Integer id, String nombre, double precio, int stock) {
        Repuesto r = new Repuesto();
        r.setId(id);
        r.setNombreRepuesto(nombre);
        r.setPrecio(precio);
        r.setStockRepuesto(stock);
        r.setCodigoBarras("R-001");
        return r;
    }

    @Test
    void obtenerRepuestos_devuelveDTOs() {
        when(repuestoRepository.findAll()).thenReturn(List.of(nuevoRepuesto(1, "Freno", 8000.0, 5)));
        List<RepuestoDTO> resultado = repuestoService.obtenerRepuestos();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreRepuesto()).isEqualTo("Freno");
    }

    @Test
    void obtenerRepuestoDTOPorId_noExiste_lanza() {
        when(repuestoRepository.findById(5)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> repuestoService.obtenerRepuestoDTOPorId(5))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void guardarRepuesto_precioInvalido_lanza() {
        Repuesto r = nuevoRepuesto(null, "Freno", -1.0, 5);
        assertThatThrownBy(() -> repuestoService.guardarRepuesto(r))
                .isInstanceOf(IllegalArgumentException.class);
        verify(repuestoRepository, never()).save(any());
    }

    @Test
    void guardarRepuesto_valido_persiste() {
        Repuesto r = nuevoRepuesto(null, "Freno", 8000.0, 5);
        when(repuestoRepository.save(r)).thenReturn(nuevoRepuesto(1, "Freno", 8000.0, 5));
        assertThat(repuestoService.guardarRepuesto(r).getId()).isEqualTo(1);
    }

    @Test
    void eliminarRepuesto_existe_borra() {
        when(repuestoRepository.existsById(1)).thenReturn(true);
        assertThat(repuestoService.eliminarRepuesto(1)).contains("exito");
        verify(repuestoRepository).deleteById(1);
    }

    @Test
    void obtenerRepuestosSinStockDTO_filtraStockCero() {
        when(repuestoRepository.findByStockRepuesto(0)).thenReturn(List.of(nuevoRepuesto(2, "Cable", 1000.0, 0)));
        assertThat(repuestoService.obtenerRepuestosSinStockDTO()).hasSize(1);
    }
}
