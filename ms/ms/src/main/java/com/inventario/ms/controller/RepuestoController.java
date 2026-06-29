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

import com.inventario.ms.DTO.RepuestoDTO;
import com.inventario.ms.model.Repuesto;
import com.inventario.ms.services.RepuestoService;
import com.inventario.ms.assemblers.RepuestoAssembler;

@RestController
@RequestMapping("/api/v1/repuesto")
public class RepuestoController {
    @Autowired
    private RepuestoService repuestoService;

    @Autowired
    private RepuestoAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> listarProductos(){
        List<RepuestoDTO> repuestos = repuestoService.obtenerRepuestos();
        if (repuestos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<RepuestoDTO>> repuestoModels = repuestos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<RepuestoDTO>> collectionModel = CollectionModel.of(repuestoModels,
                linkTo(methodOn(RepuestoController.class).listarProductos()).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarProductoPorId(@PathVariable Integer id) {
        try {
            RepuestoDTO repuesto = repuestoService.obtenerRepuestoDTOPorId(id);
            return ResponseEntity.ok(assembler.toModel(repuesto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/codigo/{codigoBarra}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarPorCodigoBarra(@PathVariable String codigoBarra) {
        try{
            List<RepuestoDTO> repuestos = repuestoService.buscarPorCodigoBarraDTO(codigoBarra);
            List<EntityModel<RepuestoDTO>> repuestoModels = repuestos.stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<RepuestoDTO>> collectionModel = CollectionModel.of(repuestoModels,
                    linkTo(methodOn(RepuestoController.class).buscarPorCodigoBarra(codigoBarra)).withSelfRel());
            return new ResponseEntity<>(collectionModel, HttpStatus.OK);
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> guardarRepuesto(@RequestBody Repuesto repuesto){
        try {
            Repuesto repuesto2 = repuestoService.guardarRepuesto(repuesto);
            RepuestoDTO dto = repuestoService.obtenerRepuestoDTOPorId(repuesto2.getId());
            return new ResponseEntity<>(assembler.toModel(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer id) {
        String resultado = repuestoService.eliminarRepuesto(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre){
        List<RepuestoDTO> repuestos = repuestoService.buscarPorNombreDTO(nombre);
        if(repuestos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<RepuestoDTO>> repuestoModels = repuestos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<RepuestoDTO>> collectionModel = CollectionModel.of(repuestoModels,
                linkTo(methodOn(RepuestoController.class).buscarPorNombre(nombre)).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/sin-stock", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> sinStock() {
        List<RepuestoDTO> repuestos = repuestoService.obtenerRepuestosSinStockDTO();
        if (repuestos == null || repuestos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<RepuestoDTO>> repuestoModels = repuestos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<RepuestoDTO>> collectionModel = CollectionModel.of(repuestoModels,
                linkTo(methodOn(RepuestoController.class).sinStock()).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> editarRepuestos(@PathVariable Integer id, @RequestBody Repuesto repuesto){
        try {
            Repuesto editado = repuestoService.guardarRepuesto(repuesto);
            RepuestoDTO dto = repuestoService.obtenerRepuestoDTOPorId(editado.getId());
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
