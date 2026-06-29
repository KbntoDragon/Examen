package com.bicicleta.ms3.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.bicicleta.ms3.DTO.ModeloDTO;
import com.bicicleta.ms3.controller.ModeloController;

@Component
public class ModeloAssembler implements RepresentationModelAssembler<ModeloDTO, EntityModel<ModeloDTO>> {
    @Override
        public EntityModel<ModeloDTO> toModel(ModeloDTO modelo) {
            return EntityModel.of(modelo,

                linkTo(methodOn(ModeloController.class).buscarModelo(modelo.getId())).withSelfRel(),
                linkTo(methodOn(ModeloController.class).listarModelos()).withRel("modelos"),
                linkTo(methodOn(ModeloController.class).eliminarModelo(modelo.getId())).withRel("eliminar"),
                linkTo(methodOn(ModeloController.class).actualizarModelo(modelo.getId(), null)).withRel("actualizar")
            );
        }
}
