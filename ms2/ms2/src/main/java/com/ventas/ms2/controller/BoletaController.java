package com.ventas.ms2.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventas.ms2.DTO.BoletaDTO;
import com.ventas.ms2.model.Boleta;
import com.ventas.ms2.service.BoletaService;
import com.ventas.ms2.assemblers.BoletaAssembler;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/boletas")
@Tag(name = "Boletas", description = "Gestión de boletas de venta y su comunicación con el inventario")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    @Autowired
    private BoletaAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> obtenerBoletas(){
        List<BoletaDTO> boletas = boletaService.obtenerBoletas();
        if(boletas.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<BoletaDTO>> boletaModels = boletas.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<BoletaDTO>> collectionModel = CollectionModel.of(boletaModels,
                linkTo(methodOn(BoletaController.class).obtenerBoletas()).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id){
        try {
            BoletaDTO boleta = boletaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(boleta));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> guardarBoleta(@RequestBody Boleta boleta){
        try {
            Boleta nuevaBoleta = boletaService.guardarBoleta(boleta);
            BoletaDTO boletaDTO = boletaService.convertirADTO(nuevaBoleta);
            return new ResponseEntity<>(assembler.toModel(boletaDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarBoleta(@PathVariable Integer id){
        String mensajeAlerta = boletaService.eliminar(id);
        return new ResponseEntity<>(mensajeAlerta, HttpStatus.OK);
    }

    @PostMapping(value = "/{boletaId}/productos/{productoId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> agregarProducto(
            @PathVariable Integer boletaId,
            @PathVariable Integer productoId){
        try {
            Boleta boleta = boletaService.agregarProducto(boletaId, productoId);
            BoletaDTO boletaDTO = boletaService.convertirADTO(boleta);
            return ResponseEntity.ok(assembler.toModel(boletaDTO));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
