package com.ventas.ms2.assemblers;

import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import com.ventas.ms2.DTO.TipoPagoDTO;
import com.ventas.ms2.controller.TipoPagoController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TipoPagoAssembler implements RepresentationModelAssembler<TipoPagoDTO, EntityModel<TipoPagoDTO>> {

    @Override
    public EntityModel<TipoPagoDTO> toModel(TipoPagoDTO tipoPago) {
        return EntityModel.of(tipoPago,
            linkTo(methodOn(TipoPagoController.class).buscarServicioPorId(tipoPago.getId())).withSelfRel(),
            linkTo(methodOn(TipoPagoController.class).listarTipoPago()).withRel("tipoPagos"));
    }
}
