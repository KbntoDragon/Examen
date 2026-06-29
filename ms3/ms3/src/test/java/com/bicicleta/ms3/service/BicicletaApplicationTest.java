package com.bicicleta.ms3.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bicicleta.ms3.DTO.BicicletaDTO;
import com.bicicleta.ms3.DTO.ClienteExternoDTO;
import com.bicicleta.ms3.model.Bicicleta;
import com.bicicleta.ms3.repository.BicicletaRepository;

@ExtendWith(MockitoExtension.class)
public class BicicletaApplicationTest {

    @Mock
    private BicicletaRepository bicicletaRepository;

    @Mock
    private BicicletaValidaciones bicicletaValidaciones;

    @InjectMocks
    private BicicletaService bicicletaService;

    private Bicicleta bicicletaPrueba;
    private ClienteExternoDTO clienteMock;

    @BeforeEach
    void setUp() {
        bicicletaPrueba = new Bicicleta();
        bicicletaPrueba.setId(1);
        bicicletaPrueba.setMaterial("Fibra de Carbono");

        clienteMock = new ClienteExternoDTO();
        clienteMock.setClienteId(100);
        clienteMock.setNombreCliente("Carlos Mendoza");
    }

    @Test
    void testBuscarPorId_Exito() {

        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicletaPrueba));
        when(bicicletaValidaciones.validarCliente(1)).thenReturn(clienteMock);

        BicicletaDTO resultado = bicicletaService.buscarPorId(1);


        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Fibra de Carbono", resultado.getMaterial());
        assertEquals("Carlos Mendoza", resultado.getNombreCliente()); 

        verify(bicicletaRepository, times(1)).findById(1);
        verify(bicicletaValidaciones, times(1)).validarCliente(1);
    }

    @Test
    void testBuscarPorId_NoEncontrado() {
        when(bicicletaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bicicletaService.buscarPorId(99);
        });


        assertEquals("Bicicleta no encontrada en los archivos", exception.getMessage());
    }

    @Test
    void testObtenerTodas() {

        Bicicleta bici2 = new Bicicleta();
        bici2.setId(2);
        bici2.setMaterial("Aluminio");
        
        when(bicicletaRepository.findAll()).thenReturn(Arrays.asList(bicicletaPrueba, bici2));
        when(bicicletaValidaciones.validarCliente(anyInt())).thenReturn(clienteMock);

        List<BicicletaDTO> resultados = bicicletaService.obtenerTodas();
        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        assertEquals("Fibra de Carbono", resultados.get(0).getMaterial());
        assertEquals("Aluminio", resultados.get(1).getMaterial());
    }

    @Test
    void testEliminar_Exito() {
        when(bicicletaRepository.findById(1)).thenReturn(Optional.of(bicicletaPrueba));
        doNothing().when(bicicletaRepository).delete(bicicletaPrueba);

        String mensaje = bicicletaService.eliminar(1);

        assertEquals("Bicicleta eliminada con exito", mensaje);
        verify(bicicletaRepository, times(1)).delete(bicicletaPrueba);
    }

    @Test
    void testEliminar_NoExiste() {
        when(bicicletaRepository.findById(99)).thenReturn(Optional.empty());

        String mensaje = bicicletaService.eliminar(99);

        assertEquals("No se puede eliminar, la bicicleta no existe con el id:99", mensaje);
        verify(bicicletaRepository, never()).delete(any(Bicicleta.class));
    }
}