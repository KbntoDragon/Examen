package com.inventario.ms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventario.ms.DTO.RepuestoDTO;
import com.inventario.ms.model.Repuesto;
import com.inventario.ms.repository.RepuestoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class RepuestoService {

    @Autowired
    private RepuestoRepository repuestoRepository;

    private RepuestoDTO convertirADTO(Repuesto repuesto) {
        RepuestoDTO dto = new RepuestoDTO();
        dto.setId(repuesto.getId());
        dto.setNombreRepuesto(repuesto.getNombreRepuesto());
        dto.setPrecio(repuesto.getPrecio());
        dto.setStockRepuesto(repuesto.getStockRepuesto());
        dto.setCodigoBarras(repuesto.getCodigoBarras());
        dto.setBoleta_id(repuesto.getBoleta_id());
        return dto;
    }

    public List<RepuestoDTO> obtenerRepuestos() {
        log.info("Obteniendo todos los repuestos");
        return repuestoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public RepuestoDTO obtenerRepuestoDTOPorId(Integer id) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado con id: " + id));
        return convertirADTO(repuesto);
    }

    public List<RepuestoDTO> buscarPorCodigoBarraDTO(String codigoBarra) {
        return repuestoRepository.findByCodigoBarras(codigoBarra).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<RepuestoDTO> buscarPorNombreDTO(String nombre) {
        return repuestoRepository.findByNombreRepuestoContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<RepuestoDTO> obtenerRepuestosSinStockDTO() {
        return repuestoRepository.findByStockRepuesto(0).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Repuesto guardarRepuesto(Repuesto repuesto) {
        if (repuesto.getPrecio() == null || repuesto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (repuesto.getStockRepuesto() == null || repuesto.getStockRepuesto() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        return repuestoRepository.save(repuesto);
    }

    public String eliminarRepuesto(Integer id) {
        if (!repuestoRepository.existsById(id)) {
            return "Repuesto no encontrado con id: " + id;
        }
        repuestoRepository.deleteById(id);
        return "Repuesto eliminado con exito";
    }
}
