package com.ventas.ms2.service;

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

import com.ventas.ms2.DTO.TipoPagoDTO;
import com.ventas.ms2.model.TipoPago;
import com.ventas.ms2.repository.TipoPagoRepository;

@ExtendWith(MockitoExtension.class)
class TipoPagoServiceTest {

    @Mock
    private TipoPagoRepository tipoPagoRepository;

    @InjectMocks
    private TipoPagoService tipoPagoService;

    private TipoPago nuevoTipoPago(Integer id, String tipo) {
        TipoPago tp = new TipoPago();
        tp.setId(id);
        tp.setTipo(tipo);
        return tp;
    }

    @Test
    void obtenerTipoPago_devuelveDTOs() {
        when(tipoPagoRepository.findAll()).thenReturn(List.of(nuevoTipoPago(1, "Debito")));
        List<TipoPagoDTO> resultado = tipoPagoService.obtenerTipoPago();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("Debito");
    }

    @Test
    void obtenerTipoPagoDTOPorId_noExiste_lanza() {
        when(tipoPagoRepository.findById(8)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tipoPagoService.obtenerTipoPagoDTOPorId(8))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void guardarTipoPago_vacio_lanza() {
        TipoPago tp = nuevoTipoPago(null, "  ");
        assertThatThrownBy(() -> tipoPagoService.guardarTipoPago(tp))
                .isInstanceOf(IllegalArgumentException.class);
        verify(tipoPagoRepository, never()).save(any());
    }

    @Test
    void guardarTipoPago_valido_persiste() {
        TipoPago tp = nuevoTipoPago(null, "Credito");
        when(tipoPagoRepository.save(tp)).thenReturn(nuevoTipoPago(1, "Credito"));
        assertThat(tipoPagoService.guardarTipoPago(tp).getId()).isEqualTo(1);
    }
}
