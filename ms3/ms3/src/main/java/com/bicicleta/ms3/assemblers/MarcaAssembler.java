package com.bicicleta.ms3.assemblers;

import com.bicicleta.ms3.controller.MarcaController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.bicicleta.ms3.DTO.MarcaDTO;

@Component
public class MarcaAssembler implements RepresentationModelAssembler<MarcaDTO, EntityModel<MarcaDTO>> {
    @Override
        public EntityModel<MarcaDTO> toModel(MarcaDTO marca) {
            return EntityModel.of(marca,
                linkTo(methodOn(MarcaController.class).buscarPorId(marca.getId())).withSelfRel(),
                linkTo(methodOn(MarcaController.class).todasLasMarcas()).withRel("marcas"),
                linkTo(methodOn(MarcaController.class).eliminarMarca(marca.getId())).withRel("eliminar"),
                linkTo(methodOn(MarcaController.class).editarMarca(marca.getId(), null)).withRel("patch"),
                linkTo(methodOn(MarcaController.class).actualizarMarca(marca.getId(), null)).withRel("actualizar")
            );
        }
}
