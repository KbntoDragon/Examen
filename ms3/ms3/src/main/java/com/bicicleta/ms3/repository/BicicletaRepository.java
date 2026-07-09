package com.bicicleta.ms3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicicleta.ms3.model.Bicicleta;


@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Integer> {
    List<Bicicleta> findByClienteId(Integer clienteId);
    List<Bicicleta> findByModeloId(Integer modeloId);
    List<Bicicleta> findByMaterial(String material);
    List<Bicicleta> findByMarcaId(Integer marcaId);
}
