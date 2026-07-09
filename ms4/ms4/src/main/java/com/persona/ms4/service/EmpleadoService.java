package com.persona.ms4.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.persona.ms4.DTO.EmpleadoDTO;
import com.persona.ms4.modelo.Empleado;
import com.persona.ms4.repository.EmpleadoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<EmpleadoDTO> findAll() {
        List<EmpleadoDTO> listaDTOs = new ArrayList<>();
        List<Empleado> empleadosReales = empleadoRepository.findAll();
        for (Empleado emp : empleadosReales) {
            listaDTOs.add(convertirADTO(emp));
        }
        return listaDTOs;
    }

    public EmpleadoDTO buscarPorId(Integer id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con id: " + id));
        return convertirADTO(empleado);
    }

    public EmpleadoDTO guardarEmpleado(Empleado nuevoEmpleado) {
        Empleado guardado = empleadoRepository.save(nuevoEmpleado);
        return convertirADTO(guardado);
    }

    public List<Empleado> buscarPorNombre(String nombres) {
        List<Empleado> empleados = empleadoRepository.findByNombresContainingIgnoreCase(nombres);

        if (empleados.isEmpty()) {
            throw new RuntimeException("No existen empleados con el nombre: " + nombres);
        }
        return empleados;
    }

    public String eliminarPorId(Integer id) {
        try {
            Empleado empleado = empleadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede eliminar, el empleado no existe con id: " + id));
            empleadoRepository.delete(empleado);
            return ("Empleado eliminado exitosamente");     

        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Empleado actualizarEmpleado(Integer id, Empleado empleadoActualizado) {
        Empleado empleado = empleadoRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encuentra el empleado."));
        if(empleado != null) {
            empleado.setNombres(empleadoActualizado.getNombres());
            empleado.setApellidos(empleadoActualizado.getApellidos());
            return empleadoRepository.save(empleado);
        }
        return null;
    }

    public EmpleadoDTO convertirADTO(Empleado empleado) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(empleado.getId());
        dto.setNombres(empleado.getNombres());
        dto.setApellidos(empleado.getApellidos());
        return dto;
    }

}
