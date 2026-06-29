package com.inventario.ms.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;

import com.inventario.ms.DTO.ProductoDTO;
import com.inventario.ms.assemblers.ProductoAssembler;
import com.inventario.ms.model.Producto;
import com.inventario.ms.services.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @Mock
    private ProductoAssembler assembler;

    @InjectMocks
    private ProductoController productoController;

    private ProductoDTO dto() {
        ProductoDTO d = new ProductoDTO();
        d.setId(1);
        d.setNombreProducto("Cadena");
        return d;
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        lenient().when(assembler.toModel(any(ProductoDTO.class)))
                 .thenAnswer(inv -> EntityModel.of((ProductoDTO) inv.getArgument(0)));
    }

    @Test
    void listar_conDatos_devuelve200() {
        when(productoService.obtenerProductos()).thenReturn(List.of(dto()));
        assertThat(productoController.listarProductos().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void listar_vacio_devuelve204() {
        when(productoService.obtenerProductos()).thenReturn(List.of());
        assertThat(productoController.listarProductos().getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void buscarPorId_ok_devuelve200() {
        when(productoService.obtenerProductoDTOPorId(1)).thenReturn(dto());
        assertThat(productoController.buscarProductoPorId(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void buscarPorId_noExiste_devuelve404() {
        when(productoService.obtenerProductoDTOPorId(99)).thenThrow(new RuntimeException("no encontrado"));
        assertThat(productoController.buscarProductoPorId(99).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void guardar_ok_devuelve201() {
        Producto p = new Producto();
        p.setId(1);
        when(productoService.guardarProducto(p)).thenReturn(p);
        when(productoService.obtenerProductoDTOPorId(1)).thenReturn(dto());
        assertThat(productoController.guardarProducto(p).getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void guardar_invalido_devuelve400() {
        Producto p = new Producto();
        when(productoService.guardarProducto(p)).thenThrow(new IllegalArgumentException("precio"));
        assertThat(productoController.guardarProducto(p).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void eliminar_exito_devuelve200() {
        when(productoService.eliminarProducto(1)).thenReturn("Producto eliminado con exito");
        assertThat(productoController.eliminarProducto(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void eliminar_noExiste_devuelve404() {
        when(productoService.eliminarProducto(9)).thenReturn("Producto no encontrado con id: 9");
        assertThat(productoController.eliminarProducto(9).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void buscarPorNombre_vacio_devuelve204() {
        when(productoService.buscarPorNombreDTO("x")).thenReturn(List.of());
        assertThat(productoController.buscarPorNombre("x").getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void buscarPorCodigo_ok_devuelve200() {
        when(productoService.buscarPorCodigoDeBarraDTO("ABC")).thenReturn(List.of(dto()));
        assertThat(productoController.buscarPorCodigoBarra("ABC").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void sinStock_ok_devuelve200() {
        when(productoService.obtenerProductoSinStockDTO()).thenReturn(List.of(dto()));
        assertThat(productoController.sinStock().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void editar_ok_devuelve200() {
        Producto p = new Producto();
        p.setId(1);
        when(productoService.guardarProducto(p)).thenReturn(p);
        when(productoService.obtenerProductoDTOPorId(1)).thenReturn(dto());
        assertThat(productoController.editarProducto(1, p).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
