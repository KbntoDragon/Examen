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

import com.bicicleta.ms3.DTO.ModeloDTO;
import com.bicicleta.ms3.model.Modelo;
import com.bicicleta.ms3.service.ModeloService;
import com.bicicleta.ms3.assemblers.ModeloAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/modelos")
@Tag(name = "Modelos", description = "Gestión de los modelos de bicicletas")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private ModeloAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar todos los modelos")
    public ResponseEntity<?> listarModelos() { // Coincide con ModeloAssembler
        List<ModeloDTO> modelos = modeloService.obtenerModelos(); // Nota: En tu service escribiste 'obtainerModelos' o 'obtenerModelos', cámbialo según corresponda
        if (modelos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EntityModel<ModeloDTO>> modeloModels = modelos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ModeloDTO>> collectionModel = CollectionModel.of(modeloModels,
                linkTo(methodOn(ModeloController.class).listarModelos()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar un modelo por su ID")
    public ResponseEntity<?> buscarModelo(@PathVariable Integer id) { // Coincide con ModeloAssembler
        try {
            ModeloDTO dto = modeloService.obtenerModeloPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar un nuevo modelo")
    public ResponseEntity<?> registrarModelo(@Valid @RequestBody Modelo nuevoModelo) {
        try {
            Modelo modeloGuardado = modeloService.guardarModelo(nuevoModelo);
            // Tu service no tiene el convertirADTO público, pero podemos buscarlo por ID para obtener el DTO estructurado
            ModeloDTO dto = modeloService.obtenerModeloPorId(modeloGuardado.getId());
            return new ResponseEntity<>(assembler.toModel(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un modelo completo")
    public ResponseEntity<?> actualizarModelo(@PathVariable Integer id, @Valid @RequestBody Modelo modeloActualizado) {
        try {
            Modelo modeloEditado = modeloService.actualizarModelo(id, modeloActualizado);
            ModeloDTO dto = modeloService.obtenerModeloPorId(modeloEditado.getId());
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un modelo")
    public ResponseEntity<?> eliminarModelo(@PathVariable Integer id) {
        try {
            String mensaje = modeloService.eliminarModelo(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}