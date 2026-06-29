package com.inventario.ms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventario.ms.model.Repuesto;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Integer> {

    List<Repuesto> findByNombreRepuestoContainingIgnoreCase(String nombre);

    List<Repuesto> findByCodigoBarras(String codigoBarras);

    List<Repuesto> findByStockRepuesto(Integer stockRepuesto);
}
