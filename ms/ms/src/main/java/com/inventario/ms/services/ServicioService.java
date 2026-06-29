package com.inventario.ms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.ms.DTO.ServicioDTO;
import com.inventario.ms.model.Servicio;
import com.inventario.ms.repository.ServicioRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    private ServicioDTO convertirADTO(Servicio servicio) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(servicio.getId());
        dto.setNombreServicio(servicio.getNombreServicio());
        dto.setDescServicio(servicio.getDescServicio());
        dto.setValorDelServicio(servicio.getValorDelServicio());
        dto.setBoleta_id(servicio.getBoleta_id());
        return dto;
    }

    public List<ServicioDTO> obtenerServicios() {
        log.info("Obteniendo todos los servicios");
        return servicioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<ServicioDTO> buscarPorNombreDTO(String nombre) {
        return servicioRepository.findByNombreServicioContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ServicioDTO obtenerServicioDTOPorId(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));
        return convertirADTO(servicio);
    }

    public Servicio guardarServicio(Servicio servicio) {
        if (servicio.getValorDelServicio() == null || servicio.getValorDelServicio() <= 0) {
            throw new IllegalArgumentException("El valor del servicio debe ser mayor a 0");
        }
        return servicioRepository.save(servicio);
    }

    public String eliminarServicio(Integer id) {
        if (!servicioRepository.existsById(id)) {
            return "Servicio no encontrado con id: " + id;
        }
        servicioRepository.deleteById(id);
        return "Servicio eliminado con exito";
    }
}
