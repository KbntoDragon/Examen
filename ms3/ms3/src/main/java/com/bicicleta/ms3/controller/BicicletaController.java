package com.bicicleta.ms3.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bicicleta.ms3.DTO.BicicletaDTO;
import com.bicicleta.ms3.model.Bicicleta;
import com.bicicleta.ms3.service.BicicletaService;
import com.bicicleta.ms3.assemblers.BicicletaAssembler; 

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/bicicletas")
@Tag(name = "Bicicletas", description = "Gestión de bicicletas, búsqueda por cliente, modelo, marca y material")
public class BicicletaController {
    
    @Autowired
    private BicicletaService bicicletaService;

    @Autowired
    private BicicletaAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las bicicletas", description = "Retorna una lista con todas las bicicletas registradas")
    public ResponseEntity<?> listar() { // Se llama listar() para que coincida con tu Assembler
        List<BicicletaDTO> bicicletas = bicicletaService.obtenerTodas(); 
        if (bicicletas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EntityModel<BicicletaDTO>> bicicletaModels = bicicletas.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<BicicletaDTO>> collectionModel = CollectionModel.of(bicicletaModels,
                linkTo(methodOn(BicicletaController.class).listar()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener bicicleta por id", description = "Retorna una bicicleta en base a su ID único")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            BicicletaDTO dto = bicicletaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar una nueva bicicleta", description = "Crea una bicicleta en la base de datos")
    public ResponseEntity<?> registrar(@Valid @RequestBody Bicicleta bicicleta) {
        try {
            BicicletaDTO dto = bicicletaService.guardar(bicicleta);
            return new ResponseEntity<>(assembler.toModel(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error en la transmisión de datos: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar la bicicleta completa", description = "Actualiza la bicicleta completa con sus atributos")
    public ResponseEntity<?> actualizarBicicleta(@Valid @RequestBody Bicicleta bicicleta, @PathVariable Integer id) {
        try {
            // El servicio retorna Entidad, por lo que buscamos el DTO para el HATEOAS
            Bicicleta nuevaBici = bicicletaService.actualizarBicicleta(id, bicicleta);
            BicicletaDTO dto = bicicletaService.buscarPorId(nuevaBici.getId());
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una bicicleta", description = "Remueve una bicicleta del sistema por su ID")
    public ResponseEntity<?> eliminarBicicleta(@PathVariable Integer id) {
        try {
            String mensajeAlerta = bicicletaService.eliminar(id);
            return new ResponseEntity<>(mensajeAlerta, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(value = "/cliente/{clienteId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar bicicletas por cliente")
    public ResponseEntity<?> buscarPorCliente(@PathVariable Integer clienteId) {
        return new ResponseEntity<>("Falta crear buscarPorCliente en BicicletaService", HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(value = "/modelo/{modeloId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar bicicletas por modelo")
    public ResponseEntity<?> buscarPorModelo(@PathVariable Integer modeloId) {
        return new ResponseEntity<>("Falta crear buscarPorModelo en BicicletaService", HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(value = "/material/{material}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar bicicletas por material")
    public ResponseEntity<?> buscarPorMaterial(@PathVariable String material) {
        return new ResponseEntity<>("Falta crear buscarPorMaterial en BicicletaService", HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(value = "/marca/{marcaId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar bicicletas por marca")
    public ResponseEntity<?> buscarPorMarca(@PathVariable Integer marcaId) {
        return new ResponseEntity<>("Falta crear buscarPorMarca en BicicletaService", HttpStatus.NOT_IMPLEMENTED);
    }
}