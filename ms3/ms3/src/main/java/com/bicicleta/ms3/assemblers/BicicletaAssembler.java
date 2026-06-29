package com.bicicleta.ms3.assemblers;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.bicicleta.ms3.DTO.BicicletaDTO;
import com.bicicleta.ms3.controller.BicicletaController;

@Component
public class BicicletaAssembler implements RepresentationModelAssembler<BicicletaDTO, EntityModel<BicicletaDTO>>{
@Override
    public EntityModel<BicicletaDTO> toModel(BicicletaDTO bicicleta) {
        return EntityModel.of(bicicleta,
            linkTo(methodOn(BicicletaController.class).buscarPorId(bicicleta.getId())).withSelfRel(),
            linkTo(methodOn(BicicletaController.class).listar()).withRel("bicicletas"),
            linkTo(methodOn(BicicletaController.class).buscarPorCliente(null)).withRel("buscar-por-cliente"),
            linkTo(methodOn(BicicletaController.class).buscarPorMarca(null)).withRel("buscar-por-marca"),
            linkTo(methodOn(BicicletaController.class).eliminarBicicleta(bicicleta.getId())).withRel("eliminar"),
            linkTo(methodOn(BicicletaController.class).actualizarBicicleta(null, bicicleta.getId())).withRel("actualizar"));
    }
}
