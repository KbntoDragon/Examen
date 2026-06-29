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

import com.bicicleta.ms3.DTO.ColorDTO;
import com.bicicleta.ms3.model.Color;
import com.bicicleta.ms3.service.ColorService;
import com.bicicleta.ms3.assemblers.ColorAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/colores")
@Tag(name = "Colores", description = "Gestión de los colores de las bicicletas")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @Autowired
    private ColorAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los colores")
    public ResponseEntity<?> todosLosColores() { // Coincide con ColorAssembler
        List<ColorDTO> colores = colorService.obtenerColores();
        if (colores.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EntityModel<ColorDTO>> colorModels = colores.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ColorDTO>> collectionModel = CollectionModel.of(colorModels,
                linkTo(methodOn(ColorController.class).todosLosColores()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar color por ID")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            ColorDTO dto = colorService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar un nuevo color")
    public ResponseEntity<?> registrarColor(@Valid @RequestBody Color nuevoColor) {
        try {
            ColorDTO dto = colorService.guardar(nuevoColor);
            return new ResponseEntity<>(assembler.toModel(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un color completo")
    public ResponseEntity<?> actualizarColor(@PathVariable Integer id, @Valid @RequestBody Color colorActualizado) {
        try {
            Color colorEditado = colorService.actualizarColor(id, colorActualizado);
            ColorDTO dto = colorService.convertirADTO(colorEditado);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Editar parcialmente un color")
    public ResponseEntity<?> editarColor(@PathVariable Integer id, @RequestBody Color colorParcial) {
        try {
            // Reutilizamos actualizarColor ya que tu Service maneja la edición de propiedades básicas
            Color colorEditado = colorService.actualizarColor(id, colorParcial);
            ColorDTO dto = colorService.convertirADTO(colorEditado);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un color")
    public ResponseEntity<?> eliminarColor(@PathVariable Integer id) {
        try {
            String mensaje = colorService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}