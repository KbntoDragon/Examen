package com.inventario.ms.controller;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.ms.DTO.ServicioDTO;
import com.inventario.ms.model.Servicio;
import com.inventario.ms.services.ServicioService;
import com.inventario.ms.assemblers.ServicioAssembler;

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {
    @Autowired
    private ServicioService servicioService;

    @Autowired
    private ServicioAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> listarServicios(){
        List<ServicioDTO> servicios = servicioService.obtenerServicios();
        if (servicios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<ServicioDTO>> servicioModels = servicios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<ServicioDTO>> collectionModel = CollectionModel.of(servicioModels,
                linkTo(methodOn(ServicioController.class).listarServicios()).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre){
        List<ServicioDTO> servicios = servicioService.buscarPorNombreDTO(nombre);
        if (servicios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<ServicioDTO>> servicioModels = servicios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<ServicioDTO>> collectionModel = CollectionModel.of(servicioModels,
                linkTo(methodOn(ServicioController.class).buscarPorNombre(nombre)).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarProductoPorId(@PathVariable Integer id){
        try {
            ServicioDTO servicio = servicioService.obtenerServicioDTOPorId(id);
            return ResponseEntity.ok(assembler.toModel(servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> guardarServicio(@RequestBody Servicio servicio){
        try {
            Servicio servicio2 = servicioService.guardarServicio(servicio);
            ServicioDTO dto = servicioService.obtenerServicioDTOPorId(servicio2.getId());
            return new ResponseEntity<>(assembler.toModel(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarServicio(@PathVariable Integer id){
        String resultado = servicioService.eliminarServicio(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> editarServicio(@PathVariable Integer id, @RequestBody Servicio servicio){
        try {
            Servicio editado = servicioService.guardarServicio(servicio);
            ServicioDTO dto = servicioService.obtenerServicioDTOPorId(editado.getId());
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
