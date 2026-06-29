package com.bicicleta.ms3.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import com.bicicleta.ms3.DTO.ColorDTO;
import com.bicicleta.ms3.controller.ColorController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ColorAssembler implements RepresentationModelAssembler<ColorDTO, EntityModel<ColorDTO>> {

    @Override
    public EntityModel<ColorDTO> toModel(ColorDTO color) {
        return EntityModel.of(color,
            linkTo(methodOn(ColorController.class).buscarPorId(color.getId())).withSelfRel(),
            linkTo(methodOn(ColorController.class).todosLosColores()).withRel("colores"),
            linkTo(methodOn(ColorController.class).eliminarColor(color.getId())).withRel("eliminar"),
            linkTo(methodOn(ColorController.class).editarColor(color.getId(), null)).withRel("patch"),
            linkTo(methodOn(ColorController.class).actualizarColor(color.getId(), null)).withRel("actualizar"));
    } 
}
