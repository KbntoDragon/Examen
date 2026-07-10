package com.persona.ms4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.persona.ms4.DTO.EmpleadoDTO;
import com.persona.ms4.modelo.Empleado;
import com.persona.ms4.repository.EmpleadoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EmpleadoService {
    
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<EmpleadoDTO> obtenerTodos() {
        log.info("Obteniendo todos los datos de los Empleados");
        return empleadoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public EmpleadoDTO buscarPorId(Integer id) {
        log.info("Buscando al Empleado por ID", id);
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado o no existe"));
        return convertirADTO(empleado);
    }

    public EmpleadoDTO guardarEmpleado(Empleado empleado) {
        log.info("Eliminando un Empleado del sistema");
        return convertirADTO(empleadoRepository.save(empleado));
    }

    public String eliminar(Integer id) {
        log.info("Eliminando un Empleado del sistema");
        try {
            Empleado empleado = empleadoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("No es posible eliminar, el Empleado con el ID: " + id + "no existe"));
            empleadoRepository.delete(empleado);
            return "El Empleado " + empleado.getNombres() + " ha sido eliminado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public EmpleadoDTO actualizarEmpleado(Integer id, Empleado empleadoActualizado) {
        log.info("Actualizando informacion del Empleado con ID: ", id);
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        empleado.setNombres(empleadoActualizado.getNombres());
        empleado.setApellidos(empleadoActualizado.getApellidos());
        return convertirADTO(empleadoRepository.save(empleado));
    }

    public List<EmpleadoDTO> buscarPorNombres(String nombre) {
        return empleadoRepository.findByNombresContainingIgnoreCase(nombre).stream()
               .map(this::convertirADTO)
               .toList();
    }

    private EmpleadoDTO convertirADTO(Empleado empleado) {
        EmpleadoDTO dto = new EmpleadoDTO();

        dto.setId(empleado.getId());
        dto.setNombres(empleado.getNombres());
        dto.setApellidos(empleado.getApellidos());
        return dto;
    }

}