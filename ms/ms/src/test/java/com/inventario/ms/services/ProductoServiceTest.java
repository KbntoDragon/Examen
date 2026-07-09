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

import com.inventario.ms.DTO.ProductoDTO;
import com.inventario.ms.model.Producto;
import com.inventario.ms.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto nuevoProducto(Integer id, String nombre, double precio, int stock) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombreProducto(nombre);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setCodigoBarras("ABC123");
        return p;
    }

    @Test
    void obtenerProductos_devuelveListaDeDTOs() {
        when(productoRepository.findAll())
                .thenReturn(List.of(nuevoProducto(1, "Cadena", 5000.0, 10)));
        List<ProductoDTO> resultado = productoService.obtenerProductos();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreProducto()).isEqualTo("Cadena");
    }

    @Test
    void obtenerProductoDTOPorId_cuandoExiste_devuelveDTO() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(nuevoProducto(1, "Cadena", 5000.0, 10)));
        ProductoDTO dto = productoService.obtenerProductoDTOPorId(1);
        assertThat(dto.getId()).isEqualTo(1);
    }

    @Test
    void obtenerProductoDTOPorId_cuandoNoExiste_lanzaExcepcion() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> productoService.obtenerProductoDTOPorId(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    void guardarProducto_conDatosValidos_persiste() {
        Producto p = nuevoProducto(null, "Cadena", 5000.0, 10);
        when(productoRepository.save(p)).thenReturn(nuevoProducto(1, "Cadena", 5000.0, 10));
        Producto guardado = productoService.guardarProducto(p);
        assertThat(guardado.getId()).isEqualTo(1);
        verify(productoRepository).save(p);
    }

    @Test
    void guardarProducto_conPrecioInvalido_noPersisteYlanza() {
        Producto p = nuevoProducto(null, "Cadena", 0.0, 10);
        assertThatThrownBy(() -> productoService.guardarProducto(p))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio");
        verify(productoRepository, never()).save(any());
    }

    @Test
    void guardarProducto_conStockNegativo_lanza() {
        Producto p = nuevoProducto(null, "Cadena", 5000.0, -1);
        assertThatThrownBy(() -> productoService.guardarProducto(p))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("stock");
    }

    @Test
    void eliminarProducto_cuandoExiste_devuelveExito() {
        when(productoRepository.existsById(1)).thenReturn(true);
        String mensaje = productoService.eliminarProducto(1);
        assertThat(mensaje).contains("exito");
        verify(productoRepository).deleteById(1);
    }

    @Test
    void eliminarProducto_cuandoNoExiste_noBorra() {
        when(productoRepository.existsById(99)).thenReturn(false);
        String mensaje = productoService.eliminarProducto(99);
        assertThat(mensaje).contains("no encontrado");
        verify(productoRepository, never()).deleteById(any());
    }

    @Test
    void buscarPorNombreDTO_devuelveCoincidencias() {
        when(productoRepository.findByNombreProductoContainingIgnoreCase("cad"))
                .thenReturn(List.of(nuevoProducto(1, "Cadena", 5000.0, 10)));
        List<ProductoDTO> resultado = productoService.buscarPorNombreDTO("cad");
        assertThat(resultado).hasSize(1);
    }

    @Test
    void obtenerProductoSinStockDTO_filtraStockCero() {
        when(productoRepository.findByStock(0))
                .thenReturn(List.of(nuevoProducto(2, "Pedal", 3000.0, 0)));
        List<ProductoDTO> resultado = productoService.obtenerProductoSinStockDTO();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getStock()).isZero();
    }
}
