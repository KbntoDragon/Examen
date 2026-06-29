package com.bicicleta.ms3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bicicleta.ms3.DTO.BicicletaDTO;
import com.bicicleta.ms3.DTO.ClienteExternoDTO;
import com.bicicleta.ms3.model.Bicicleta;
import com.bicicleta.ms3.model.Color;
import com.bicicleta.ms3.repository.BicicletaRepository;



@Service
public class BicicletaService {

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private BicicletaValidaciones bicicletaValidaciones;

    @Autowired
    private WebClient.Builder webClientBuilder;
    
    public List<BicicletaDTO> obtenerTodas() {
        List<BicicletaDTO> listaDTOs = new ArrayList<>();
        List<Bicicleta> bicicletasReales = bicicletaRepository.findAll();
        for (Bicicleta bici : bicicletasReales) {
            listaDTOs.add(convertirADTO(bici));
        }
        return listaDTOs;
    }

    public BicicletaDTO buscarPorId(Integer id) {
        Bicicleta bici = bicicletaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada en los archivos"));
        return convertirADTO(bici);
    }

    public BicicletaDTO guardar(Bicicleta nuevaBicicleta) {
        if (bicicletaValidaciones.validarNull(nuevaBicicleta)) {
            Bicicleta guardada = bicicletaRepository.save(nuevaBicicleta);
            return convertirADTO(guardada);
        }
        throw new RuntimeException("Datos de la bicicleta inválidos.");
    }
    
    
    public Bicicleta actualizarBicicleta(Integer id, Bicicleta bicicletaDatos){
        Bicicleta bici = bicicletaRepository.findById(id).orElseThrow(() -> new RuntimeException("No se puede actualizar, la bicicleta no existe con los registros"));
        if(bicicletaDatos.getMaterial() != null){
            bici.setMaterial(bicicletaDatos.getMaterial());
        }
        if (bicicletaDatos.getColores() != null) {
            bici.setColores(bicicletaDatos.getColores());
        }
        if (bicicletaDatos.getMarca() != null) {
            bici.setMarca(bicicletaDatos.getMarca());
        }
        if (bicicletaDatos.getModelo() != null) {
            bici.setModelo(bicicletaDatos.getModelo());
        }
        return bicicletaRepository.save(bici);
    }

    public String eliminar(Integer id){
        try{
            Bicicleta bicicleta = bicicletaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede eliminar, la bicicleta no existe con el id:" + id));
            bicicletaRepository.delete(bicicleta);
            return "Bicicleta eliminada con exito";
        } catch (RuntimeException e){
            return e.getMessage();
        }
    }
    
    private BicicletaDTO convertirADTO(Bicicleta bici) {
        BicicletaDTO dto = new BicicletaDTO();
        dto.setId(bici.getId());
        dto.setMaterial(bici.getMaterial());
        ClienteExternoDTO cliente = bicicletaValidaciones.validarCliente(bici.getId());
        dto.setNombreCliente(cliente.getNombreCliente());

        if (bici.getModelo() != null) {
            dto.setModeloNombre(bici.getModelo().getNombreModelo()); 
        }

        if (bici.getMarca() != null) {
            dto.setMarcas(bici.getMarca().getNombre()); 
        }
        if (bici.getColores() != null) {
            String nombresColores = bici.getColores().stream()
                .map(Color::getNombre)
                .collect(Collectors.joining(", "));
            dto.setColores(nombresColores);
        }
        return dto;
    }
}
