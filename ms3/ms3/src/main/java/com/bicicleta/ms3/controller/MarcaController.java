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

import com.bicicleta.ms3.DTO.MarcaDTO;
import com.bicicleta.ms3.model.Marca;
import com.bicicleta.ms3.service.MarcaService;
import com.bicicleta.ms3.assemblers.MarcaAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/marcas")
@Tag(name = "Marcas", description = "Gestión de las marcas de bicicletas")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private MarcaAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las marcas")
    public ResponseEntity<?> todasLasMarcas() { // Coincide con MarcaAssembler
        List<MarcaDTO> marcas = marcaService.obtenerMarcas();
        if (marcas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EntityModel<MarcaDTO>> marcaModels = marcas.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<MarcaDTO>> collectionModel = CollectionModel.of(marcaModels,
                linkTo(methodOn(MarcaController.class).todasLasMarcas()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar marca por ID")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            MarcaDTO dto = marcaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar una nueva marca")
    public ResponseEntity<?> registrarMarca(@Valid @RequestBody Marca nuevaMarca) {
        try {
            Marca marcaGuardada = marcaService.guardarMarca(nuevaMarca);
            MarcaDTO dto = marcaService.convertirADTO(marcaGuardada);
            return new ResponseEntity<>(assembler.toModel(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una marca completa")
    public ResponseEntity<?> actualizarMarca(@PathVariable Integer id, @Valid @RequestBody Marca marcaActualizada) {
        try {
            Marca marcaEditada = marcaService.actualizarMarca(id, marcaActualizada);
            MarcaDTO dto = marcaService.convertirADTO(marcaEditada);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Editar parcialmente una marca")
    public ResponseEntity<?> editarMarca(@PathVariable Integer id, @RequestBody Marca marcaParcial) {
        try {
            Marca marcaEditada = marcaService.actualizarMarca(id, marcaParcial);
            MarcaDTO dto = marcaService.convertirADTO(marcaEditada);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una marca")
    public ResponseEntity<?> eliminarMarca(@PathVariable Integer id) {
        try {
            String mensaje = marcaService.eliminar(id);
            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}