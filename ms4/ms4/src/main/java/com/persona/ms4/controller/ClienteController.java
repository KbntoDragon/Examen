package com.persona.ms4.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.persona.ms4.DTO.ClienteDTO;
import com.persona.ms4.assemblers.ClienteModelAssembler;
import com.persona.ms4.modelo.Cliente;
import com.persona.ms4.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadas con los Clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los Clientes", description = "Se obtiene un listado de todos los Clientes")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<ClienteDTO>> clientes = clienteService.obtenerTodos().stream()
               .map(assembler::toModel)
               .collect(Collectors.toList());

        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un Cliente por su ID", description = "Obtiene el Cliente por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            ClienteDTO dto = clienteService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/nombres/{nombres}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar Clientes por nombre", description = "Obtiene un listado de Clientes que existen con el nombre ingresado")
    public ResponseEntity<?> buscarPorNombres(@PathVariable String nombres) {
        List<EntityModel<ClienteDTO>> clientes = clienteService.buscarPorNombres(nombres).stream()
              .map(assembler::toModel)
              .collect(Collectors.toList());

        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).buscarPorNombres(nombres)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("clientes")));
    }

    @GetMapping(value = "/correos/{correos}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar Clientes por correo", description = "Obtiene un listado de Clientes que existen con el correo ingresado")
    public ResponseEntity<?> buscraPorCorreos(@PathVariable String correos) {
        List<EntityModel<ClienteDTO>> clientes = clienteService.buscarPorCorreos(correos).stream()
              .map(assembler::toModel)
              .collect(Collectors.toList());

        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).buscraPorCorreos(correos)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("clientes")));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un Cliente al sistema", description = "Agrega un Cliente a la base de datos")
    public ResponseEntity<?> agregarCliente(@Valid @RequestBody Cliente cliente) {
        try {
            ClienteDTO dto = clienteService.guardarCliente(cliente);
            return ResponseEntity
                .created(linkTo(methodOn(ClienteController.class).buscarPorId(dto.getId())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un Cliente", description = "Elimina un Cliente de la base de datos")
    public ResponseEntity<?> eliminarCliente(@PathVariable Integer id) {
        String resultado = clienteService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el Cliente completo", description = "Actualiza el Cliente completo con sus atributos")
    public ResponseEntity<?> actualiarCliente(@PathVariable Integer id, @Valid @RequestBody Cliente cliente) {
        try {
            cliente.setId(id);
            ClienteDTO dto = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}