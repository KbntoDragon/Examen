package com.bicicleta.ms3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.bicicleta.ms3.DTO.MarcaDTO;
import com.bicicleta.ms3.model.Marca;
import com.bicicleta.ms3.repository.MarcaRepository;

import net.datafaker.Faker;

@SpringBootTest
public class MarcaApplicationTests {
@Autowired
    private MarcaService marcaService;

    @MockitoBean
    private MarcaRepository marcaRepository;

    private Faker faker = new Faker();

    private Marca createMarca() {
        Marca marca = new Marca();
        marca.setId(faker.number().positive());
        marca.setNombre(faker.company().name());
        return marca;
    }

    @Test
    public void testObtenerMarcas() {
        Marca marca1 = createMarca();
        Marca marca2 = createMarca();
        when(marcaRepository.findAll()).thenReturn(List.of(marca1, marca2));

        List<MarcaDTO> resultado = marcaService.obtenerMarcas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testBuscarPorId() {
        Marca marca = createMarca();
        when(marcaRepository.findById(1)).thenReturn(Optional.of(marca));

        MarcaDTO resultado = marcaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(marca.getNombre(), resultado.getNombre());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(marcaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> marcaService.buscarPorId(99));
    }

    @Test
    public void testGuardarMarca() {
        Marca marca = createMarca();
        when(marcaRepository.save(any(Marca.class))).thenReturn(marca);

        Marca resultado = marcaService.guardarMarca(marca);

        assertNotNull(resultado);
        assertEquals(marca.getNombre(), resultado.getNombre());
    }
}
