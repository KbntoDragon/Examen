package com.bicicleta.ms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicicleta.ms3.model.Modelo;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Integer> {

}
