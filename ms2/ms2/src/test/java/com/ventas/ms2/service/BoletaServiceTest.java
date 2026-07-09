package com.ventas.ms2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.ventas.ms2.DTO.BoletaDTO;
import com.ventas.ms2.DTO.ProductoDTO;
import com.ventas.ms2.model.Boleta;
import com.ventas.ms2.model.TipoPago;
import com.ventas.ms2.repository.BoletaRepository;

@ExtendWith(MockitoExtension.class)
class BoletaServiceTest {

    @Mock
    private BoletaRepository boletaRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private BoletaService boletaService;

    private Boleta nuevaBoleta(Integer id, double total) {
        Boleta b = new Boleta();
        b.setId(id);
        b.setTotal(total);
        TipoPago tp = new TipoPago();
        tp.setTipo("Efectivo");
        b.setTipoPago(tp);
        return b;
    }

    @Test
    void obtenerBoletas_mapeaTotalAprecio() {
        when(boletaRepository.findAll()).thenReturn(List.of(nuevaBoleta(1, 12000.0)));
        List<BoletaDTO> resultado = boletaService.obtenerBoletas();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPrecio()).isEqualTo(12000.0);
        assertThat(resultado.get(0).getTipoPago()).isEqualTo("Efectivo");
    }

    @Test
    void convertirADTO_sinTipoPago_dejaNull() {
        Boleta b = new Boleta();
        b.setId(2);
        b.setTotal(500.0);
        BoletaDTO dto = boletaService.convertirADTO(b);
        assertThat(dto.getTipoPago()).isNull();
        assertThat(dto.getPrecio()).isEqualTo(500.0);
    }

    @Test
    void buscarPorId_noExiste_lanza() {
        when(boletaRepository.findById(5)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> boletaService.buscarPorId(5))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void guardarBoleta_totalNegativo_lanza() {
        Boleta b = nuevaBoleta(null, -1.0);
        assertThatThrownBy(() -> boletaService.guardarBoleta(b))
                .isInstanceOf(IllegalArgumentException.class);
        verify(boletaRepository, never()).save(any());
    }

    @Test
    void guardarBoleta_valida_persiste() {
        Boleta b = nuevaBoleta(null, 12000.0);
        when(boletaRepository.save(b)).thenReturn(nuevaBoleta(1, 12000.0));
        assertThat(boletaService.guardarBoleta(b).getId()).isEqualTo(1);
    }

    @Test
    void eliminar_existe_borra() {
        when(boletaRepository.existsById(1)).thenReturn(true);
        assertThat(boletaService.eliminar(1)).contains("exito");
        verify(boletaRepository).deleteById(1);
    }

    @Test
    void eliminar_noExiste_devuelveMensaje() {
        when(boletaRepository.existsById(9)).thenReturn(false);
        assertThat(boletaService.eliminar(9)).contains("no encontrada");
        verify(boletaRepository, never()).deleteById(any());
    }

    @Test
    void agregarProducto_cuandoProductoExiste_loAgregaALaBoleta() {
        Boleta boleta = nuevaBoleta(1, 1000.0);
        boleta.setProductoIds(new ArrayList<>());
        when(boletaRepository.findById(1)).thenReturn(Optional.of(boleta));
        when(boletaRepository.save(any(Boleta.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoDTO productoRemoto = new ProductoDTO();
        productoRemoto.setId(2);
        when(webClientBuilder.build().get()
                .uri(anyString(), any(Object.class))
                .retrieve()
                .onStatus(any(), any())
                .bodyToMono(ProductoDTO.class)
                .block()).thenReturn(productoRemoto);

        Boleta resultado = boletaService.agregarProducto(1, 2);

        assertThat(resultado.getProductoIds()).contains(2);
    }

    @Test
    void agregarProducto_cuandoBoletaNoExiste_lanza() {
        when(boletaRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> boletaService.agregarProducto(99, 2))
                .isInstanceOf(RuntimeException.class);
    }
}
