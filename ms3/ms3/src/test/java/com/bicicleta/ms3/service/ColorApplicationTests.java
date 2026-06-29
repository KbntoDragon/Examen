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

import com.bicicleta.ms3.DTO.ColorDTO;
import com.bicicleta.ms3.model.Color;
import com.bicicleta.ms3.repository.ColorRepository;

import net.datafaker.Faker;

@SpringBootTest
public class ColorApplicationTests {
@Autowired
    private ColorService colorService;

    @MockitoBean
    private ColorRepository colorRepository;

    private Faker faker = new Faker();

    private Color createColor() {
        Color color = new Color();
        color.setId(faker.number().positive());
        color.setNombre(faker.color().name());
        return color;
    }

    @Test
    public void testObtenerColores() {
        Color color1 = createColor();
        Color color2 = createColor();
        when(colorRepository.findAll()).thenReturn(List.of(color1, color2));

        List<ColorDTO> resultado = colorService.obtenerColores();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    public void testBuscarPorId() {
        Color color = createColor();
        when(colorRepository.findById(1)).thenReturn(Optional.of(color));

        ColorDTO resultado = colorService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(color.getNombre(), resultado.getNombre());
    }

    @Test
    public void testBuscarPorIdNoExiste() {
        when(colorRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> colorService.buscarPorId(99));
    }

    @Test
    public void testGuardarColor() {
    Color color = createColor();
    when(colorRepository.save(any(Color.class))).thenReturn(color);

    ColorDTO resultado = colorService.guardar(color);

    assertNotNull(resultado);
    assertEquals(color.getNombre(), resultado.getNombre());
}
}
