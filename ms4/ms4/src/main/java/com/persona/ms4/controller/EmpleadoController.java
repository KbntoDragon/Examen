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

import com.persona.ms4.DTO.EmpleadoDTO;
import com.persona.ms4.assemblers.EmpleadoModelAssembler;
import com.persona.ms4.modelo.Empleado;
import com.persona.ms4.service.EmpleadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping ("/api/v1/empleados")
@Tag (name = "Empleados", description = "Operaciones relacionadas a los Empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoModelAssembler assembler;
/* 
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los empleados", description = "Se obtiene un listado de todos los empleados")
    public ResponseEntity<?> obtenerTodos() {
        List<EntityModel<EmpleadoDTO>> empleados = empleadoService.obtenerTodo().stream()
               .map(assembler::toModel)
               .collect(Collectors.toList());

        if (empleados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(empleados,
                linkTo(methodOn(EmpleadoController.class).obtenerTodos()).withSelfRel()));
    }*/

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener un Empleado por su ID", description = "Obtiene el Empleado por el ID ingresado")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            EmpleadoDTO dto = empleadoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
/* 
    @GetMapping(value = "/nombres/{nombres}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar Empleados por nombre", description = "Obtiene un listado de Empleados que existen con el nombre ingresado")
    public ResponseEntity<?> buscarPorNombres(@PathVariable String nombres) {
        List<EntityModel<EmpleadoDTO>> empleados = empleadoService.buscarPorNombres(nombres).stream()
              .map(assembler::toModel)
              .collect(Collectors.toList());

        if (empleados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(CollectionModel.of(empleados,
                linkTo(methodOn(EmpleadoController.class).buscarPorNombres(nombres)).withSelfRel(),
                linkTo(methodOn(EmpleadoController.class).obtenerTodos()).withRel("empleados")));
    }*/

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Agregar un Empleado al sistema", description = "Agrega un Empleado a la base de datos")
    public ResponseEntity<?> agregarEmpleado(@Valid @RequestBody Empleado empleado) {
        try {
            EmpleadoDTO dto = empleadoService.guardarEmpleado(empleado);
            return ResponseEntity
                .created(linkTo(methodOn(EmpleadoController.class).buscarPorId(dto.getId())).toUri())
                .body(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*@DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un Empleado", description = "Elimina un Empleado de la base de datos")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Integer id) {
        String resultado = empleadoService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }*/

    /*@PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar el Empleado completo", description = "Actualiza el Empleado completo con sus atributos")
    public ResponseEntity<?> actualizarEmpleado(@PathVariable Integer id, @Valid @RequestBody Empleado empleado) {
        try {
            empleado.setId(id);
            EmpleadoDTO dto = empleadoService.actualizarEmpleado(id, empleado);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }*/
}
