package com.bicicleta.ms3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bicicleta.ms3.DTO.MarcaDTO;
import com.bicicleta.ms3.model.Marca;
import com.bicicleta.ms3.repository.MarcaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MarcaService {
   @Autowired
    private MarcaRepository marcaRepository;

    public List<MarcaDTO> obtenerMarcas() {
        return marcaRepository.findAll().stream().map(this::convertirADTO).toList();

    }

    public MarcaDTO buscarPorId(Integer id) {
        Marca marca = marcaRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Marca no encontrada con el id: " + id));
        return convertirADTO(marca);
    }

    public Marca buscarPorNombre(String nombre) {
        return marcaRepository.findByNombre(nombre)
        .orElse(null);
    }

    public Marca guardarMarca(Marca marca) {
        return marcaRepository.save(marca);
    }

    public String eliminar(Integer id) {
        try {
            Marca marca = marcaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede eliminar, la marca no existe con el id: " + id));
            marcaRepository.delete(marca);
            return ("Marca eliminada exitosamente");
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Marca actualizarMarca(Integer id, Marca marcaActualizado) {
        Marca marca = marcaRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encuentra la Marca"));
        if(marca != null) {
            marca.setNombre(marcaActualizado.getNombre());
            return marcaRepository.save(marca);
        }
        return null;
    }

    
    public MarcaDTO convertirADTO(Marca marca) {
        MarcaDTO dto = new MarcaDTO();
        dto.setId(marca.getId());
        dto.setNombre(marca.getNombre());

        if (marca.getBicicletas() != null && !marca.getBicicletas().isEmpty()) {
            dto.setBicicletas(marca.getBicicletas().stream().map(bicicleta -> bicicleta.getId()).toList());
        }
        return dto;
    }
}
