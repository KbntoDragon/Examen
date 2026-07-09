package com.bicicleta.ms3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bicicleta.ms3.DTO.ModeloDTO;
import com.bicicleta.ms3.model.Modelo;
import com.bicicleta.ms3.repository.ModeloRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ModeloService {
    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Modelo guardarModelo(Modelo modelo){
        return modeloRepository.save(modelo);
    }

    private ModeloDTO convertirADTO(Modelo modelo){
        ModeloDTO dto = new ModeloDTO();
        dto.setId(modelo.getId());
        dto.setNombre(modelo.getNombreModelo());
        dto.setTipoSuspension(modelo.getTipoSuspension());
        dto.setTallaCuadro(modelo.getTallaCuadro());
        
        if (modelo.getMarca() != null) {
            dto.setMarcaNombre(modelo.getMarca().getNombre());
        }
        return dto;
    }
    public List<ModeloDTO> obtenerModelos(){
        return modeloRepository.findAll().stream()
        .map(this::convertirADTO)
        .toList();
    }

    public ModeloDTO obtenerModeloPorId(Integer id){
        Modelo modelo = modeloRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));
        return convertirADTO(modelo);
    }

    public String eliminarModelo(Integer id){
        try {
            Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡No se puede eliminar! el modelo con ID "+id+" no existe"));
            modeloRepository.delete(modelo);
            return "El modelo "+modelo.getNombreModelo()+" ha sido eliminado con exito";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Modelo actualizarModelo(Integer id, Modelo modeloActualizado) {
        Modelo modelo = modeloRepository.findById(id).orElseThrow(() -> new RuntimeException("¡No se encuentra ese modelo!"));
        if(modelo != null) {
            modelo.setNombreModelo(modeloActualizado.getNombreModelo());
            modelo.setTipoSuspension(modeloActualizado.getTipoSuspension());
            modelo.setTallaCuadro(modeloActualizado.getTallaCuadro());
            modelo.setMarca(modeloActualizado.getMarca());
            return modeloRepository.save(modelo);
        }
        return null;
    }
}
