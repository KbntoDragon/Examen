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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ventas.ms2.DTO.TipoPagoDTO;
import com.ventas.ms2.model.TipoPago;
import com.ventas.ms2.service.TipoPagoService;
import com.ventas.ms2.assemblers.TipoPagoAssembler;

@RestController
@RequestMapping("/api/v1/tipoPago")
public class TipoPagoController {
    @Autowired
    private TipoPagoService tipoPagoService;

    @Autowired
    private TipoPagoAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> listarTipoPago(){
        List<TipoPagoDTO> tipoPagos = tipoPagoService.obtenerTipoPago();
        if (tipoPagos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<TipoPagoDTO>> tipoPagoModels = tipoPagos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<TipoPagoDTO>> collectionModel = CollectionModel.of(tipoPagoModels,
                linkTo(methodOn(TipoPagoController.class).listarTipoPago()).withSelfRel());
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> buscarServicioPorId(@PathVariable Integer id){
        try {
            TipoPagoDTO tipoPago = tipoPagoService.obtenerTipoPagoDTOPorId(id);
            return ResponseEntity.ok(assembler.toModel(tipoPago));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> guardarTipoPago(@RequestBody TipoPago tipoPago){
        try {
            TipoPago tipoPago2 = tipoPagoService.guardarTipoPago(tipoPago);
            TipoPagoDTO dto = tipoPagoService.obtenerTipoPagoDTOPorId(tipoPago2.getId());
            return new ResponseEntity<>(assembler.toModel(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
