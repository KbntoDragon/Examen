package com.bicicleta.ms3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicicleta.ms3.model.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {
    Optional<Color> findByNombre(String nombre);
}
