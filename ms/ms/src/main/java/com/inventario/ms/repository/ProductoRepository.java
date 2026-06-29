package com.inventario.ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventario.ms.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);

    List<Producto> findByCodigoBarras(String codigoBarras);

    List<Producto> findByStock(Integer stock);
}
