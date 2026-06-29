package com.ventas.ms2.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import com.ventas.ms2.DTO.BoletaDTO;
import com.ventas.ms2.controller.BoletaController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BoletaAssembler implements RepresentationModelAssembler<BoletaDTO, EntityModel<BoletaDTO>> {

    @Override
    public EntityModel<BoletaDTO> toModel(BoletaDTO boleta) {
        return EntityModel.of(boleta,
            linkTo(methodOn(BoletaController.class).buscarPorId(boleta.getId())).withSelfRel(),
            linkTo(methodOn(BoletaController.class).obtenerBoletas()).withRel("boletas"),
            linkTo(methodOn(BoletaController.class).eliminarBoleta(boleta.getId())).withRel("eliminar"));
    }
}
