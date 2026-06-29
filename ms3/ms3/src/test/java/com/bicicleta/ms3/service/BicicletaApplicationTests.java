package com.bicicleta.ms3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.bicicleta.ms3.DTO.BicicletaDTO;
import com.bicicleta.ms3.model.Bicicleta;
import com.bicicleta.ms3.repository.BicicletaRepository;

import net.datafaker.Faker;

@SpringBootTest
public class BicicletaApplicationTests {

    @MockitoBean
    private BicicletaRepository bicicletaRepository;

    @InjectMocks
    private BicicletaService bicicletaService;
    private Faker faker =  new Faker();
    @BeforeEach
    void  setUp(){
        MockitoAnnotations.openMocks(bicicletaRepository);
    }

    @Test
    void testBuscarPorId(){
        Integer idSimulado = 10;
		Bicicleta biciFalso = new Bicicleta();
		biciFalso.setId(idSimulado);
		// Entrenamos al Mock: Cuando el repositorio busque este ID, responderá con nuestro Jedifalso
		when(bicicletaRepository.findById(idSimulado)).thenReturn(Optional.of(biciFalso));
		// WHEN: Cuando ejecutamos la acción del servicio que queremos evaluar

		BicicletaDTO resultado = bicicletaService.buscarPorId(idSimulado);
		// THEN: Entonces validamos que las compuertas de datos funcionen de forma idónea
		assertNotNull(resultado, "El DTO resultante no debería ser nulo");
		// Verificamos que el servicio realmente haya consultado al repositorio exactamente 1 vez
		verify(bicicletaRepository, times(1)).findById(idSimulado);
	}
}
