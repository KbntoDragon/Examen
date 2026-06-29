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

import com.bicicleta.ms3.DTO.ModeloDTO;
import com.bicicleta.ms3.model.Modelo;
import com.bicicleta.ms3.repository.ModeloRepository;

import net.datafaker.Faker;

@SpringBootTest
public class ModeloApplicationTests {
    
    @Autowired
    private ModeloService modeloService;

    @MockitoBean
    private ModeloRepository modeloRepository;

    private Faker faker = new Faker();

    private Modelo createModelo() {
        Modelo modelo = new Modelo();
        modelo.setId(faker.number().positive());
        modelo.setNombreModelo(faker.commerce().productName());
        return modelo;
    }

    @Test
    public void testObtenerModelos() {
        Modelo modelo1 = createModelo();
        Modelo modelo2 = createModelo();
        when(modeloRepository.findAll()).thenReturn(List.of(modelo1, modelo2));

        List<ModeloDTO> resultado = modeloService.obtenerModelos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testObtenerModeloPorId() {
        Modelo modelo = createModelo();
        when(modeloRepository.findById(1)).thenReturn(Optional.of(modelo));

        ModeloDTO resultado = modeloService.obtenerModeloPorId(1);

        assertNotNull(resultado);
        assertEquals(modelo.getNombreModelo(), resultado.getNombre());
    }

    @Test
    public void testObtenerModeloPorIdNoExiste() {
        when(modeloRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> modeloService.obtenerModeloPorId(99));
    }

    @Test
    public void testGuardarModelo() {
        Modelo modelo = createModelo();
        when(modeloRepository.save(any(Modelo.class))).thenReturn(modelo);

        Modelo resultado = modeloService.guardarModelo(modelo);

        assertNotNull(resultado);
        assertEquals(modelo.getNombreModelo(), resultado.getNombreModelo());
    }

}
