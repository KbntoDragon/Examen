package com.persona.ms4.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.persona.ms4.modelo.Empleado;


@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer>{

    List<Empleado> findByNombresContainingIgnoreCase(String nombres);

}
