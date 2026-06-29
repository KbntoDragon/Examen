package com.inventario.ms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.ms.DTO.ProductoDTO;
import com.inventario.ms.model.Producto;
import com.inventario.ms.repository.ProductoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Convierte la entidad a su DTO de salida (no exponemos la entidad directamente)
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCodigoBarras(producto.getCodigoBarras());
        dto.setBoleta_id(producto.getBoleta_id());
        return dto;
    }

    public List<ProductoDTO> obtenerProductos() {
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ProductoDTO obtenerProductoDTOPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        return convertirADTO(producto);
    }

    public List<ProductoDTO> buscarPorCodigoDeBarraDTO(String codigoBarra) {
        return productoRepository.findByCodigoBarras(codigoBarra).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<ProductoDTO> buscarPorNombreDTO(String nombre) {
        return productoRepository.findByNombreProductoContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<ProductoDTO> obtenerProductoSinStockDTO() {
        return productoRepository.findByStock(0).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Producto guardarProducto(Producto producto) {
        // Reglas de negocio del dominio inventario
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        return productoRepository.save(producto);
    }

    public String eliminarProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            return "Producto no encontrado con id: " + id;
        }
        productoRepository.deleteById(id);
        return "Producto eliminado con exito";
    }
}
